package com.auzmor.servlet;

import com.auzmor.constants.ConfigurationConstants;
import com.auzmor.util.RedisUtil;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 20/08/18.
 */
class OutboundSMSServletIT {

    @BeforeEach
    void setUp() throws IOException
    {
        ConfigurationConstants.getConfigProperties().load(new FileInputStream(new File("src/main/webapp/WEB-INF/config.properties")));

        RedisUtil.getJedis().flushAll();

    }

    @Test
    void testStopRestriction() throws InterruptedException
    {
        RestAssured.defaultParser = Parser.JSON;

        //sending inbound stop sms
        Response response1 = RestAssured.given()
                .header("Authorization", "Basic YXpyMToyMFMwS1BOT0lN")
                .body("{\"from\":\"441224980094\", \"to\":\"4924195509198\", \"text\":\"STOP\"}")
                .when().post("http://localhost:7070/inbound/sms");

        assertEquals(200, response1.statusCode());
        assertEquals("inbound sms ok", response1.jsonPath().getString("message"));

        //trying to send outbound sms to blocked number
        Response response2 = RestAssured.given()
                .header("Authorization", "Basic YXpyMToyMFMwS1BOT0lN")
                .body("{\"to\":\"441224980094\", \"from\":\"4924195509198\", \"text\":\"I am disturbing\"}")
                .when().post("http://localhost:7070/outbound/sms");

        assertEquals(200, response2.statusCode());
        assertEquals("sms from 4924195509198 to 441224980094 blocked by STOP request", response2.jsonPath().getString("error"));

        //wait for a second more than the block time
        Thread.sleep((ConfigurationConstants.getBlockTimeLimit() + 1) * 1000);

        //Trying to send the message after block time limit
        Response response3 = RestAssured.given()
                .header("Authorization", "Basic YXpyMToyMFMwS1BOT0lN")
                .body("{\"to\":\"441224980094\", \"from\":\"4924195509198\", \"text\":\"I have waited\"}")
                .when().post("http://localhost:7070/outbound/sms");

        assertEquals(200, response3.statusCode());
        assertEquals("outbound sms ok", response3.jsonPath().getString("message"));


    }

    @Test
    void testRateLimiting() throws InterruptedException
    {
        RestAssured.defaultParser = Parser.JSON;

        //Send max no of requests
        int maxReq = ConfigurationConstants.getMaxRequests();
        for (int i = 0; i < maxReq; i++)
        {
            RestAssured.given()
                    .header("Authorization", "Basic YXpyMToyMFMwS1BOT0lN")
                    .body("{\"to\":\"441224980094\", \"from\":\"4924195509198\", \"text\":\"Hi again\"}")
                    .when().post("http://localhost:7070/outbound/sms");
        }

        //check if requests are blocked
        Response response = RestAssured.given()
                .header("Authorization", "Basic YXpyMToyMFMwS1BOT0lN")
                .body("{\"to\":\"441224980094\", \"from\":\"4924195509198\", \"text\":\"I should be blocked\"}")
                .when().post("http://localhost:7070/outbound/sms");

        assertEquals(200, response.statusCode());
        assertEquals("limit reached for from 4924195509198", response.jsonPath().getString("error"));

        //wait for a second more than the time limit
        Thread.sleep((ConfigurationConstants.getTimlimitSeconds() + 1) * 1000);

        //after the time limit the request should pass
        Response response2 = RestAssured.given()
                .header("Authorization", "Basic YXpyMToyMFMwS1BOT0lN")
                .body("{\"to\":\"441224980094\", \"from\":\"4924195509198\", \"text\":\"I have waited\"}")
                .when().post("http://localhost:7070/outbound/sms");

        assertEquals(200, response2.statusCode());
        assertEquals("outbound sms ok", response2.jsonPath().getString("message"));

    }
}