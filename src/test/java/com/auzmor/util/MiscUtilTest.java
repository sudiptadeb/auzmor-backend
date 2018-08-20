package com.auzmor.util;

import com.auzmor.constants.ConfigurationConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 20/08/18.
 */
class MiscUtilTest {
    @BeforeEach
    void setUp() throws IOException
    {
        ConfigurationConstants.getConfigProperties().load(new FileInputStream(new File("src/main/webapp/WEB-INF/config.properties")));
    }
    @Test
    void streamToString()
    {
        InputStream inputStream = new ByteArrayInputStream("some random string".getBytes());
        assertEquals("some random string",MiscUtil.streamToString(inputStream),"Should be equal");
    }

    @Test
    void caseInsensitiveMatch()
    {
        assertTrue(MiscUtil.caseInsensitiveMatch("string", "StrinG"), "Should match");
        assertFalse(MiscUtil.caseInsensitiveMatch("string", "string1"), "Should not match");
    }

}