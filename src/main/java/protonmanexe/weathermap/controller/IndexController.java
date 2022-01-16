package protonmanexe.weathermap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/")
public class IndexController {
 
    @GetMapping // Getmapping is needed to allow landing page to be index
                // since "/" path is defined in the request mapping path
    public String showHomePage () {
        return "index";
    }
    
    @PostMapping // Getmapping is not used to avoid a "?" symbol in the address
    public String returnHomePage () {
        return "index";
    }

}