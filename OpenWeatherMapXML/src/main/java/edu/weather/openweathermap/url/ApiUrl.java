package edu.weather.openweathermap.url;

/**
 * ApiUrl class describe weather
 * api url and it`s parameters.
 */
public class ApiUrl {
    private static final String API_URL_WITHOUT_PARAMETERS = "http://api.openweathermap.org/data/2.5/";

    private static double latitude = 59.8944444;
    private static double longitude = 30.2641667;
    private static int forecastDatesCount = 16;
    private static String apiKey = "76670a24603dadd7ec1be77f8d4ece2a";

    /**
     * Get url, for getting now weather from api
     *
     * @return - api url with parameters, for getting
     *           now weather.
     */
    public static String getUrlForWeatherNow() {
        //Example parameters: weather?lat=35&lon=139&appid=76670a24603dadd7ec1be77f8d4ece2a
        String parameters = "weather?lat=" + latitude + "&lon=" + longitude + "&units=metric" + "&mode=xml"
                + "&appid=" + apiKey;
        return API_URL_WITHOUT_PARAMETERS + parameters;
    }

    /**
     * Get url, for getting weather forecast from api
     *
     * @return - api url with parameters, for getting
     *           forecast weather.
     */
    public static String getUrlForWeatherForecast() {
        ///Example parameters: http://api.openweathermap.org/data/2.5/forecast/daily?lat=35&lon=139&cnt=16&&appid=76670a24603dadd7ec1be77f8d4ece2a
        String parameters = "forecast/daily?lat=" + latitude + "&lon=" + longitude + "&cnt=" + forecastDatesCount
                + "&units=metric" + "&mode=xml" + "&appid=" + apiKey;
        return API_URL_WITHOUT_PARAMETERS + parameters;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        ApiUrl.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        ApiUrl.longitude = longitude;
    }

    public static int getForecastDatesCount() {
        return forecastDatesCount;
    }

    public static void setForecastDatesCount(int forecastDatesCount) {
        ApiUrl.forecastDatesCount = forecastDatesCount;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static void setApiKey(String apiKey) {
        ApiUrl.apiKey = apiKey;
    }
}
