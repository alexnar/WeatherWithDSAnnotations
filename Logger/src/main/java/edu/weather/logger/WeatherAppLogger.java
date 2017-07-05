package edu.weather.logger;

import java.io.IOException;
import java.util.logging.*;

/**
 * Weather application global logger
 */
public class WeatherAppLogger {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String LOGGER_SETUP_ERROR_MESSAGE = "Logger setup error!";
    private static final String LOG_FILE_PATH = "../weather-log.txt";
    private static final boolean APPEND_LOG_FILE_MODE_ON = true;

    static {
        setupLogger();
    }

    private static void setupLogger() {
        logger.removeHandler(new ConsoleHandler());
        try {
            FileHandler fileHandler = new FileHandler(LOG_FILE_PATH, APPEND_LOG_FILE_MODE_ON);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.WARNING, LOGGER_SETUP_ERROR_MESSAGE, e);
        }
    }

    public static Logger getLogger() {
        return logger;
    }


}
