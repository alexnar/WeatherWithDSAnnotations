package edu.weather.filewriter.impl;

import edu.weather.filewriter.parameters.FileProperties;
import edu.weather.filewriter.service.FileWriterService;
import edu.weather.logger.WeatherAppLogger;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component(name = "FileWriterServiceImpl", immediate = true)
@Service(value = FileWriterService.class)
public class FileWriterServiceImpl implements FileWriterService {
    private static final Logger LOGGER = WeatherAppLogger.getLogger();
    private static final String WRITE_FILE_EXCEPTION = "ERROR WHILE WRITING TO FILE";
    private static final String CHARSET = "UTF-8";

    public void write(List<String> lines) {
        String pathStr = FileProperties.getPathToFile();
        Path path = Paths.get(pathStr);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, lines, Charset.forName(CHARSET));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, WRITE_FILE_EXCEPTION, e);
        }
    }
}
