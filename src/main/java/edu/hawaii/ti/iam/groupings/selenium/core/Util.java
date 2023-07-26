package edu.hawaii.ti.iam.groupings.selenium.core;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Util {

    private static final Log logger = LogFactory.getLog(Util.class);

    public static String encodeUrl(String value) {
        String result = null;
        try {
            result = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            logger.warn("Error encoding url; " + e);
        }
        return result;
    }
}
