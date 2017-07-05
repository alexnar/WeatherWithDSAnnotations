package edu.weather.filewriter.parameters;

public class FileProperties {
    private static String pathToFile = "../weather/weather.txt";

    public static String getPathToFile() {
        return pathToFile;
    }

    public static void setPathToFile(String pathToFile) {
        FileProperties.pathToFile = pathToFile;
    }
}
