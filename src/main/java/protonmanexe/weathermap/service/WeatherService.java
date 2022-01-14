package protonmanexe.weathermap.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import protonmanexe.weathermap.model.Weather;

@Service
public class WeatherService {

    // variable declaration
    public static final String URL_WEATHER = "http://api.openweathermap.org/data/2.5/weather";
    private final static Logger logging = LoggerFactory.getLogger(WeatherService.class);
    List<Weather> list = new ArrayList<>();
 
    public Weather getWeatherDetails (String location) {

        // object instantiation
        Weather w = new Weather();
        logging.info("appkey > " +w.getAppKey());

        // build url and call for weather details, getting json object
        final String url = UriComponentsBuilder
                .fromUriString(URL_WEATHER)
                .queryParam("q", location)
                .queryParam("appid", w.getAppKey())
                .queryParam("units", "metric")
                .toUriString();
        final RequestEntity<Void> req = RequestEntity.get(url).build();
        final RestTemplate template = new RestTemplate();
        final ResponseEntity<String> resp = template.exchange(req, String.class);

        if (resp.getStatusCode() != HttpStatus.OK) // if bad response
            throw new IllegalArgumentException(
                "Error: status code %s".formatted(resp.getStatusCode().toString())
            );
        final String body = resp.getBody(); // if ok response

        logging.info("payload: %s".formatted(body));

        // convert weather json object into weather object
        try (InputStream is = new ByteArrayInputStream(body.getBytes())) {
            final JsonReader reader = Json.createReader(is);
            final JsonObject result = reader.readObject();
            final JsonArray readings = result.getJsonArray("weather");
            logging.info("readings > " +readings);

            final String cityName = result.getString("name");
            logging.info("try loop cityname > " +cityName);

            final double temperature = 
                result.getJsonObject("main").getJsonNumber("temp").doubleValue();
            logging.info("try loop temperature > " +temperature);

            final int humidity = 
                result.getJsonObject("main").getJsonNumber("humidity").intValue();
            logging.info("try loop humidity > " +humidity);

            List<Weather> list = readings.stream()
                .map(v -> (JsonObject) v)
                .map(Weather::create)
                .map(x -> {
                    x.setThisLocation(cityName);
                    x.setTemp(temperature);
                    x.setHumidity(humidity);
                    return x;
                })
                .collect(Collectors.toList());
                
            w = list.get(0);
            logging.info("city > " +w.getThisLocation());

        } catch (Exception ex) { }

        logging.info("city 2 > " +w.getThisLocation());
        logging.info("main 2 > " +w.getMain());
        logging.info("desciption 2 > " +w.getDescription());
        logging.info("temperature 2 > " +w.getTemp());
        logging.info("humidity 2 > " +w.getHumidity());
        logging.info("icon 2 > " +w.getIcon());

        return w;
    }

}