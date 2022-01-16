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
    public static final String URL_WEATHER = "https://api.openweathermap.org/data/2.5/weather";
    public static final String URL_ICON = "http://openweathermap.org/img/wn/";
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
                .queryParam("appid", w.getAppKey()) // get API key
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
            final JsonArray jweather = result.getJsonArray("weather");

            final String cityName = result.getString("name");
            final double temperature = 
                result.getJsonObject("main").getJsonNumber("temp").doubleValue();
            final int humidity = 
                result.getJsonObject("main").getJsonNumber("humidity").intValue();

            List<Weather> list = jweather.stream()
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

        } catch (Exception ex) { }

        // build url for weather icon, set creation time and add into weather object
        final String iconUrl = URL_ICON.concat(w.getIcon()).concat("@2x.png");
        w.setIconUrl(iconUrl); 
        w.setTimeNow();
        
        logging.info("city 2 > " +w.getThisLocation());
        logging.info("main 2 > " +w.getMain());
        logging.info("humidity 2 > " +w.getHumidity());
        logging.info("iconUrl 2 > " +w.getIconUrl());
        logging.info("time 2 > " +w.getTimeNow());

        return w; // return completed weather object

    }

    public Object doNth (String location) { // this is an extra method I created to examine
                                            // what is happening inside stream

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
            final JsonArray jweather = result.getJsonArray("weather");

            final String cityName = result.getString("name");
            // final double temperature = 
            //     result.getJsonObject("main").getJsonNumber("temp").doubleValue();
            // final int humidity = 
            //     result.getJsonObject("main").getJsonNumber("humidity").intValue();

            List<JsonObject> list = jweather.stream()
                .map(v -> (JsonObject) v)
                .collect(Collectors.toList());
            logging.info("list from JsonArray > " +list);

            List<Weather> list2 = list.stream()
                .map(Weather::create)
                .collect(Collectors.toList());
            logging.info("list2 from Weather > " +list2);

            List<Weather> list3 = list2.stream()
                .map(x -> {
                    x.setThisLocation(cityName);
                    return x;
                })
                .collect(Collectors.toList());
            logging.info("list3 from Weather > " +list3);

            w = list3.get(0);
            logging.info("city > " +w.getThisLocation());

        } catch (Exception ex) { }

        // build url for weather icon
        final String iconUrl = URL_ICON.concat(w.getIcon()).concat("@2x.png");
        w.setIconUrl(iconUrl); 

        logging.info("doNth city > " +w.getThisLocation());
        logging.info("doNth desciption > " +w.getDescription());
        logging.info("doNth temperature > " +w.getTemp());
        logging.info("doNth humidity > " +w.getHumidity());
        logging.info("doNth icon > " +w.getIcon());

        return w;
        
    }

}