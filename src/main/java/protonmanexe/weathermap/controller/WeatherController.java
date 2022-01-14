package protonmanexe.weathermap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import protonmanexe.weathermap.service.WeatherService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(path="/weather")
public class WeatherController {

    @Autowired
    WeatherService weaSvc;

    private final static Logger logging = LoggerFactory.getLogger(WeatherController.class); // instantiate logger

    @GetMapping
    public String getWeather (@RequestParam (required=true) String location, Model model) {

        logging.info("location > " +location);
        try {
            model.addAttribute("location", weaSvc.getWeatherDetails(location));
        } catch (IllegalArgumentException e) {
            model.addAttribute("", location);
            return "error";
        }
        
        return "weather";
    }
}
