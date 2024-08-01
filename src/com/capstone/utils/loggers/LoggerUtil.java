package com.capstone.utils.loggers;

import org.slf4j.Logger;

public class LoggerUtil {

    private LoggerUtil() {}

    //private static final int RESPONSE_LENGTH = Integer.parseInt(System.getProperty("logger.console.msg.length", "4096"));
    private static final int RESPONSE_LENGTH = 4096;

    private static final Logger infoLogger = StaticLoggerBinder.getSingleton().getLoggerFactory().getLogger("Info Data");
    private static final Logger infoToDebugLogger = StaticLoggerBinder.getSingleton().getLoggerFactory().getLogger("Info to Debug");
    private static final Logger debugLogger = StaticLoggerBinder.getSingleton().getLoggerFactory().getLogger("Debug Data");
    private static final Logger slackAlertLogger = StaticLoggerBinder.getSingleton().getLoggerFactory().getLogger("Slack Alert Info");

    public static void logINFO(String message) {
        if (message.length() > RESPONSE_LENGTH) {
            String sb = message.substring(0, RESPONSE_LENGTH) +
                    " ... << please refer the log file created in 'target/logs/' dir for the complete log >>";
            infoToDebugLogger.info(sb);
            logDEBUG(message);
        }
        else {
            infoLogger.info(message);
        }
    }

    public static void logINFO(String message, Object... parameters) {
        infoLogger.info(message, parameters);
    }

    public static void logSlackAlertINFO(String message, Object... parameters) {
        slackAlertLogger.info(message, parameters);
    }

    public static void logDEBUG(String message) {
        debugLogger.debug(message);
    }

    public static void logDEBUG(String message, Object... parameters) {
        debugLogger.debug(message, parameters);
    }

    public static void logWARNING(String message) {
        infoLogger.warn(message);
    }

    public static void logWARNING(String message, Throwable throwable) {
        infoLogger.warn(message, throwable);
    }

    public static void logERROR(String message) {
        infoLogger.error(message);
    }

    public static void logERROR(String message, Throwable throwable) {
        infoLogger.error(message, throwable);
    }
}
