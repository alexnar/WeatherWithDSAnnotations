package edu.weather.app.service;

public interface WeatherAppService {
    /**
     * Write now weather to file.
     * There will be written weather of
     * all registered services.
     */
    void writeWeatherNowToFile();

    /**
     * Write weather forecast to file.
     * There will be written weather of
     * all registered services.
     */
    void writeWeatherForecastToFile();
}
