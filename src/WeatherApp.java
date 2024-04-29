import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.text.DateFormatter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class WeatherApp {

    // Jackson library is used for more complex projects
    // simple.json library is used for simpler ones
    // to-do: try to implement the same thing with jackson library
    public static JSONObject getWeatherData(String locationName) {
        JSONArray locationData = getLocationData(locationName);

        // Try to improve this part - maybe not include casting but String to double
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relativehumidity_2m,weather_code,wind_speed_10m&timezone=Europe%2FBerlin";

        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.createObjectNode();
            HttpURLConnection urlConnection = fetchAPIResponse(urlString);

            if(urlConnection.getResponseCode() != 200) {
                System.out.println("Error: Could not connect");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                resultJson.append(line);
            }

            urlConnection.disconnect();

            // The result is stored as String "resultJson" so we need to Parse it to have it as a JSON Object - see if this can be improved later on
            // This is pretty ugly to be fair .. will need improvement
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");
            JSONArray time = (JSONArray) hourly.get("time");

            int index = findIndexOfCurrentTime(time);

            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((Long) weatherCode.get(index));

            JSONArray relativeHumidity = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long) relativeHumidity.get(index);


            JSONArray windspeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windSpeed = (double) windspeedData.get(index);

            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windSpeed", windSpeed);

            return weatherData;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        for(int i=0; i<timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime)) {
                return i;
            }

        }

        return 0;
    }

    public static String getCurrentTime () {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd'T'HH':00'");
        String formatedDateTime = localDateTime.format(formatter);

        return formatedDateTime;
    }

    public static JSONArray getLocationData(String location) {
        location = location.replaceAll(" ", "+");

//        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=33.767&longitude=-118.1892&hourly=temperature_2m";
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + location + "&count=10&language=en&format=json";

        try{
            HttpURLConnection conn = fetchAPIResponse(urlString);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();

                //We can either use Scanner or BufferedReader here
//                Scanner scanner = new Scanner(conn.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while((line = bufferedReader.readLine()) != null) {
                    resultJson.append(line);
                }
//                scanner.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                JSONArray locationData = (JSONArray) resultJsonObj.get("results");
                return locationData;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private static HttpURLConnection fetchAPIResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String convertWeatherCode(long weatherCode) {
        String weatherCondition = "";
        if(weatherCode == 0L) {
            weatherCondition = "Clear";
        } else if (weatherCode <= 3L && weatherCode > 0L) {
            weatherCondition = "Cloudy";
        } else if ((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L)) {
            weatherCondition = "Rain";
        } else if (weatherCode >= 71L && weatherCode <= 77L) {
            weatherCondition = "Snow";
        }
        return weatherCondition;
    }


}
