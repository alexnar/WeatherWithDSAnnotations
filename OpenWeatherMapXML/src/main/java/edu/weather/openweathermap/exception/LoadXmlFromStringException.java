package edu.weather.openweathermap.exception;

/**
 * Signals that while loading xml from string
 * LoadXmlFromStringException occurred.
 */
public class LoadXmlFromStringException extends Exception {
    public LoadXmlFromStringException(String message){
        super(message);
    }
}
