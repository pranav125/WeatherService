import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.io.IOException;

@RestController
@EnableAutoConfiguration
public class WebApplicationRestController {


    @RequestMapping("/city/{city}")
    String currentWeatherByCityName(@PathVariable("city") String city)  {
        OpenWeatherMapApi owm = new OpenWeatherMapApi(OpenWeatherMapApi.Units.METRIC, OpenWeatherMapApi.Language.ENGLISH, "7b98ec0521f6e05c11609c6c4132d73f");
        CurrentWeather london = null;
        try {
            london = owm.currentWeatherByCityName(city);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return london.getRawResponse();
    }

    @RequestMapping("/history/{city}")
    String history(@PathVariable("city") String city) {
        OpenWeatherMapApi owm = new OpenWeatherMapApi(OpenWeatherMapApi.Units.METRIC, OpenWeatherMapApi.Language.ENGLISH, "7b98ec0521f6e05c11609c6c4132d73f");
       return owm.currentWeatherHistoryByCityName(city);
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebApplicationRestController.class, args);
    }
}