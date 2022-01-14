package protonmanexe.weathermap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TryTry {

    public static void main(String[] args) {
        
        RestTemplate template = new RestTemplate();
        ResponseEntity <String> resp = template.getForEntity("url" , String.class);


    }
    
}
