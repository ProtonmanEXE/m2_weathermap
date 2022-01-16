package protonmanexe.weathermap.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import protonmanexe.weathermap.model.Weather;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class WeatherRepo {

    @Autowired
    @Qualifier("MyRedis")
    private RedisTemplate<String, Object> template;

    public void save (String location, Weather w) {
        template.opsForValue().set(location, w, 10, TimeUnit.MINUTES);
    }

    public Optional<Weather> get (String location) {
        Weather w = (Weather) template.opsForValue().get(location);
        return Optional.ofNullable(w);
    }

}