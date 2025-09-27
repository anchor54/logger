package com.logger;

public class Logger {

    private static Logger logger = null;

    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    private Logger() {
        // Do nothing
    }

    public void debug(String message) {
        System.out.println("[DEBUG] " + message);
    }

    public void info(String message) {
        System.out.println("[INFO] " + message);
    }

    public void warning(String message) {
        System.out.println("[WARNING] " + message);
    }

    public void error(String message) {
        System.out.println("[ERROR] " + message);
    }

    public void exception(String message) {
        System.out.println("[EXCEPTION] " + message);
    }
}
