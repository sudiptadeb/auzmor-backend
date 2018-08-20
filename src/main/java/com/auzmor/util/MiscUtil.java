package com.auzmor.util;

import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * This file was created by Sudipta Deb (sudi-3385) on 18/08/18.
 */
public class MiscUtil {
    private static final Logger LOGGER = Logger.getLogger(MiscUtil.class.getName()); //No I18N

    public static String streamToString(InputStream stream){
        Scanner s = new Scanner(stream).useDelimiter("\\A"); //Delimiter which makes the whole thing one token (HACK)
        String result = s.hasNext() ? s.next() : ""; //Read the single token , empty string if no token
        return result;
    }

    public static boolean caseInsensitiveMatch(String str1, String str2)
    {
        return (str1.compareToIgnoreCase(str2)==0);
    }
}
