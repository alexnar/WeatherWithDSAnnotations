package edu.weather.openweathermap.parser;

import edu.weather.api.dto.Weather;
import edu.weather.logger.WeatherAppLogger;
import edu.weather.openweathermap.exception.LoadXmlFromStringException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Xml parser class.
 * Provides methods for helping to
 * parse Weather XML
 */
public class WeatherXmlParser {
    private static final Logger LOGGER = WeatherAppLogger.getLogger();
    private static final String LOAD_XML_FROM_STRING_EXCEPTION_MESSAGE = "Can not load xml from string." +
            " Where string is xml response of API";

    private static final String COORDINATES_XML_TAG = "coord";
    private static final String LONGITUDE_XML_TAG = "lon";
    private static final String LATITUDE_XML_TAG = "lat";
    private static final String WEATHER_MAIN_INFO_XML_TAG = "main";
    private static final String TEMPERATURE_XML_TAG = "temperature";
    private static final String CLOUDS_XML_TAG = "clouds";
    private static final String CLOUDS_NAME_XML_ATTRIBUTE = "name";
    private static final String TEMPERATURE_VALUE_XML_ATTRIBUTE = "value";
    private static final String WEATHER_DESCRIPTION_ARRAY_XML_TAG = "weather";
    private static final String WEATHER_DESCRIPTION_XML_TAG = "description";
    private static final String CITY_XML_TAG = "city";
    private static final String LOCATION_XML_TAG = "location";
    private static final String LOCATION_LATITUDE_XML_ATTRIBUTE = "latitude";
    private static final String LOCATION_LONGITUDE_XML_ATTRIBUTE = "longitude";
    private static final String FORECAST_XML_TAG = "forecast";
    private static final String TIME_XML_TAG = "time";
    private static final String TEMPERATURE_XML_ATTRIBUTE = "day";
    private static final String CLOUDS_VALUE_XML_ATTRIBUTE = "value";


    /**
     * Return weather now from XML API.
     *
     * @param weatherXml - xml String, received from API
     * @return - List of Weather DTO
     */
    public static Weather getWeatherNowFromXml(StringBuilder weatherXml) {
        String weatherJsonString = weatherXml.toString();
        Document document = null;
        try {
            document = loadXmlFromString(weatherJsonString);
        } catch (LoadXmlFromStringException e) {
            LOGGER.log(Level.WARNING, LOAD_XML_FROM_STRING_EXCEPTION_MESSAGE, e);
            System.out.println(LOAD_XML_FROM_STRING_EXCEPTION_MESSAGE);
        }
        if (document == null) {
            return new Weather.WeatherBuilder().build();
        }
        Weather weather = parseWeatherNowFromXml(document);
        return weather;
    }

    /**
     * Return weather forecast from XML API.
     *
     * @param weatherXml - xml String, received from API
     * @return - List of Weather DTO
     */
    public static List<Weather> getWeatherForecastFromXml(StringBuilder weatherXml) {
        String weatherJsonString = weatherXml.toString();
        Document document = null;
        try {
            document = loadXmlFromString(weatherJsonString);
        } catch (LoadXmlFromStringException e) {
            LOGGER.log(Level.WARNING, LOAD_XML_FROM_STRING_EXCEPTION_MESSAGE, e);
            System.out.println(LOAD_XML_FROM_STRING_EXCEPTION_MESSAGE);
        }
        if (document == null) {
            return new ArrayList<>();
        }
        List<Weather> forecastWeatherList = parseWeatherForecastFromXml(document);
        return forecastWeatherList;
    }

    /**
     * Parse xml document (containing now weather) to Weather DTO.
     *
     * @param document - xml Document
     * @return Weather DTO
     */
    private static Weather parseWeatherNowFromXml(Document document) {
        Element rootElement = document.getDocumentElement();
        NodeList cityElements = rootElement.getElementsByTagName(CITY_XML_TAG);
        Element cityElement = (Element) cityElements.item(0);
        Element coordinateElement = (Element) cityElement.getElementsByTagName(COORDINATES_XML_TAG).item(0);
        String latitudeStr = coordinateElement.getAttribute(LATITUDE_XML_TAG);
        double latitude = Double.parseDouble(latitudeStr);
        String longitudeStr = coordinateElement.getAttribute(LONGITUDE_XML_TAG);
        double longitude = Double.parseDouble(longitudeStr);
        Element temperatureElement = (Element) rootElement.getElementsByTagName(TEMPERATURE_XML_TAG).item(0);
        String temperatureStr = temperatureElement.getAttribute(TEMPERATURE_VALUE_XML_ATTRIBUTE);
        double temperature = Double.parseDouble(temperatureStr);
        Element cloudsElement = (Element) rootElement.getElementsByTagName(CLOUDS_XML_TAG).item(0);
        String description = cloudsElement.getAttribute(CLOUDS_NAME_XML_ATTRIBUTE);
        Weather weather = new Weather.WeatherBuilder().setLatitude(latitude).setLongitude(longitude)
                .setWeatherDescription(description).setTemperature(temperature).build();
        return weather;
    }

    /**
     * Parse xml document (containing foreacst weather) to List of Weather DTO.
     *
     * @param document - xml Document
     * @return List of Weather DTO
     */
    private static List<Weather> parseWeatherForecastFromXml(Document document) {
        Element rootElement = document.getDocumentElement();
        Element locationElement = (Element) rootElement.getElementsByTagName(LOCATION_XML_TAG).item(0);
        locationElement = (Element) locationElement.getElementsByTagName(LOCATION_XML_TAG).item(0);
        String latitudeStr = locationElement.getAttribute(LOCATION_LATITUDE_XML_ATTRIBUTE);
        double latitude = Double.parseDouble(latitudeStr);
        String longitudeStr = locationElement.getAttribute(LOCATION_LONGITUDE_XML_ATTRIBUTE);
        double longitude = Double.parseDouble(longitudeStr);
        Element forecastElement = (Element) rootElement.getElementsByTagName(FORECAST_XML_TAG).item(0);
        NodeList forecastList = forecastElement.getElementsByTagName(TIME_XML_TAG);
        List<Weather> forecastWeatherList = new ArrayList<>();
        for (int elementId = 0; elementId < forecastList.getLength(); elementId++) {
            Element forecastEntry = (Element) forecastList.item(elementId);
            Weather weather = parseForecastEntry(forecastEntry, latitude, longitude);
            forecastWeatherList.add(weather);
        }
        return forecastWeatherList;
    }

    /**
     * Parse single entry of forecast list.
     *
     * @return - Weather DTO
     */
    private static Weather parseForecastEntry(Element entryElement, double latitude, double longitude) {
        Element temperatureElement = (Element) entryElement.getElementsByTagName(TEMPERATURE_XML_TAG).item(0);
        String temperatureStr = temperatureElement.getAttribute(TEMPERATURE_XML_ATTRIBUTE);
        double temperature = Double.parseDouble(temperatureStr);
        Element cloudsElement = (Element) entryElement.getElementsByTagName(CLOUDS_XML_TAG).item(0);
        String description = cloudsElement.getAttribute(CLOUDS_VALUE_XML_ATTRIBUTE);
        Weather weather = new Weather.WeatherBuilder().setLatitude(latitude).setLongitude(longitude)
                .setWeatherDescription(description).setTemperature(temperature).build();
        return weather;
    }


    /**
     * Convert xml string to Document
     *
     * @param xml - xml string
     * @return - XML Document
     * @throws LoadXmlFromStringException - if can not load xml from string
     */
    private static Document loadXmlFromString(String xml) throws LoadXmlFromStringException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new LoadXmlFromStringException(e.getMessage());
        }
        InputSource inputSource = new InputSource(new StringReader(xml));
        Document document;
        try {
            document = builder.parse(inputSource);
        } catch (SAXException e) {
            throw new LoadXmlFromStringException(e.getMessage());
        } catch (IOException e) {
            throw new LoadXmlFromStringException(e.getMessage());
        }
        return document;
    }

}
