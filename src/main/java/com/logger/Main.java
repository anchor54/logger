package com.logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger();
        
        logger.info("Application started");
        logger.debug("Debug information");
        logger.warning("This is a warning");
        logger.error("An error occurred");
        logger.exception("Exception details");
    }
}