package protonmanexe.weathermap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import protonmanexe.weathermap.model.Weather;
import protonmanexe.weathermap.repository.WeatherRepo;
import protonmanexe.weathermap.service.WeatherService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(path="/weather")
public class WeatherController {

    @Autowired
    WeatherService weaSvc;

    @Autowired
    WeatherRepo weaRepo;

    private final static Logger logging = LoggerFactory.getLogger(WeatherController.class); // instantiate logger

    @GetMapping
    public String getWeather (@RequestParam (required=true) String location, Model model) {

        // variable declaration
        String editedLocation = location.trim().toLowerCase();
        logging.info("location > " +editedLocation);

        // check whether location has an existing cache from redis
        Optional<Weather> opt = weaRepo.get(editedLocation);

        if (opt.isPresent()) {
            model.addAttribute("weatherobj", opt.get());
            String msg =("Weather was last cached at " +opt.get().getTimeNow() +".");
            model.addAttribute("msg", msg);            
            logging.info("data is from cache");
        }
        // if no cache, attempt to search current weather from API
        else {
            try {
                Weather w = weaSvc.getWeatherDetails(editedLocation);
                model.addAttribute("weatherobj", w);
                logging.info("data is from api");
                weaRepo.save(editedLocation, w);
                logging.info("try loop time > " +w.getTimeNow());
                logging.info("data saved successfully");
        // if search is not successful, go to error page
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("", editedLocation);
                return "error";
            }
        }

        return "weather";
    }

    @GetMapping("/nothing")
    public String doNth (@RequestParam (required=true) String location, Model model) {

        logging.info("location > " +location);
        try {
            model.addAttribute("weatherobj", weaSvc.doNth(location));
        } catch (IllegalArgumentException e) {
        }
        
        return "weather";
    }
}