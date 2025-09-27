package com.logger;

public class Main {
    public static void main(String[] args) {
        Logger.info("Application started");
        Logger.debug("Debug information");
        Logger.warning("This is a warning");
        Logger.error("An error occurred");
        Logger.exception("Exception details");
    }
}