package edu.weather.datagetter.exception;

/**
 * Signals that while getting data by http protocol
 * DataHttpGetException occurred.
 */
public class DataHttpGetException extends Exception {
    public DataHttpGetException(String message) {
       super(message);
    }

}
