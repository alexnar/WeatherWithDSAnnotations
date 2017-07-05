package edu.weather.app.impl;

import edu.weather.api.dto.Weather;
import edu.weather.api.service.WeatherApiService;
import edu.weather.app.service.WeatherAppService;
import edu.weather.filewriter.service.FileWriterService;
import edu.weather.logger.WeatherAppLogger;
import org.apache.felix.scr.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component(name = "WeatherAppServiceImpl", immediate = true)
@Service(value = Object.class)
@Properties({
        @Property(
                name = "osgi.command.scope",
                value = "weather"
        ),
        @Property(
                name = "osgi.command.function",
                value = {"writeWeatherNowToFile", "writeWeatherForecastToFile"}
        )
})
public class WeatherAppServiceImpl implements WeatherAppService {
    private static final Logger LOGGER = WeatherAppLogger.getLogger();
    private static final String FILTER_WRONG_PARAMETER = "Filter wrong parameters";
    private static final String SERVICE_DESCRIPTION = " weather service result";

    @Reference(cardinality = ReferenceCardinality.MANDATORY_MULTIPLE, bind = "bind", unbind = "unbind",
            referenceInterface = WeatherApiService.class, policy = ReferencePolicy.DYNAMIC)
    private List<WeatherApiService> weatherApiServiceList;

    @Reference
    private FileWriterService fileWriterService;

    @Override
    public void writeWeatherNowToFile() {
        List<String> fileContent = new ArrayList<>();
        for (WeatherApiService weatherApiService : weatherApiServiceList) {
            fileContent.add(weatherApiService.toString() + SERVICE_DESCRIPTION);
            Weather weather = weatherApiService.getWeatherNow();
            fileContent.add(weather.toString());
        }
        fileWriterService.write(fileContent);
    }

    @Override
    public void writeWeatherForecastToFile() {
        List<String> fileContent = new ArrayList<>();
        for (WeatherApiService weatherApiService : weatherApiServiceList) {
            fileContent.add(weatherApiService.toString() + SERVICE_DESCRIPTION);
            List<Weather> weatherForecast = weatherApiService.getWeatherForecast();
            for (Weather weather : weatherForecast) {
                fileContent.add(weather.toString());
            }
        }
        fileWriterService.write(fileContent);
    }


    /**
     * Bind service.
     * Add service in weatherApiServiceList.
     *
     * @param weatherApiService - service to bind
     */
    protected void bind(WeatherApiService weatherApiService) {
        if (weatherApiServiceList == null) {
            weatherApiServiceList = new ArrayList<>();
        }
        weatherApiServiceList.add(weatherApiService);
    }

    /**
     * Unbind service.
     * Remove service from weatherApiServiceList
     *
     * @param weatherApiService - service to remove
     */
    protected void unbind(WeatherApiService weatherApiService) {
        for (int serviceIndex = 0; serviceIndex < weatherApiServiceList.size(); serviceIndex++) {
            WeatherApiService weatherApiServiceCompared = weatherApiServiceList.get(serviceIndex);
            String weatherApiServiceComparedClassName = weatherApiServiceCompared.getClass().getName();
            String weatherApiServiceDeletedClassName = weatherApiService.getClass().getName();
            if (weatherApiServiceDeletedClassName.equals(weatherApiServiceComparedClassName)) {
                weatherApiServiceList.remove(serviceIndex);
            }
        }
    }
}
