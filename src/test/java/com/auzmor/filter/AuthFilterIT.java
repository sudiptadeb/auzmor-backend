package com.auzmor.filter;

import com.auzmor.constants.ConfigurationConstants;
import com.auzmor.constants.StringConstants;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 20/08/18.
 */
class AuthFilterIT {

    @BeforeEach
    void setUp() throws IOException
    {
        ConfigurationConstants.getConfigProperties().load(new FileInputStream(new File("src/main/webapp/WEB-INF/config.properties")));
    }

    @Test
    void doFilter()
    {
        RestAssured.get("http://localhost:7070/").then().statusCode(404); //URI not available
        RestAssured.get("http://localhost:7070/inbound/sms").then().statusCode(405); //Get method not allowed
        RestAssured.post("http://localhost:7070/inbound/sms").then().statusCode(403); //Credentials must be provided

        //Authentication Testing
        //wrong user:pass => azr1:123123 => YXpyMToxMjMxMjM
        RestAssured.given().header(StringConstants.AUTHORIZATION,"Basic YXpyMToxMjMxMjM=").post("http://localhost:7070/inbound/sms").then().statusCode(403); //Wrong Credentials

        //correct user:pass => azr1:20S0KPNOIM => YXpyMToyMFMwS1BOT0lN
        RestAssured.given().header(StringConstants.AUTHORIZATION,"Basic YXpyMToyMFMwS1BOT0lN").post("http://localhost:7070/inbound/sms").then().statusCode(200); //Wrong Credentials
    }
}