package edu.weather.datagetter.service;


import edu.weather.datagetter.exception.DataHttpGetException;

/**
 * DataGetterService interface provides methods
 * for getting data, from different sources
 */
public interface DataGetterService {
    /**
     * Get data from specified url. If process
     * gone wrong throws DataHttpGetException.
     *
     * @param url specified url
     * @return - StringBuilder, contains data that
     *           was received by url
     * @throws DataHttpGetException - if some problems with connection
     *                                or wrong url specified
     *                                or problems while data reading
     */
    StringBuilder getDataFromUrl(String url) throws DataHttpGetException;
}
