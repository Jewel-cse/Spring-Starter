package dev.start.init.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class CustomLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLogger.class);
    public static final Marker CRITICAL_MARKER = MarkerFactory.getMarker("CRITICAL");
    public void critical(String msg) {
        LOGGER.error(CRITICAL_MARKER, msg);
    }

    public void critical(String msg, Throwable t) {
        LOGGER.error(CRITICAL_MARKER, msg, t);
    }

    public void critical(String format, String method, Object args) {
        LOGGER.error(CRITICAL_MARKER,format,method,args);
    }

    public void critical(String format, String method, Object response, long l) {
        LOGGER.error(CRITICAL_MARKER,format,method,response,l);
    }
}


