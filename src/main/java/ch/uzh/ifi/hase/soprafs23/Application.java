package ch.uzh.ifi.hase.soprafs23;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String helloWorld() {
      /*
            [KADEN] ADDED/EDITED FOR Github Secret Key

            For running on local machine, set an env global variable INVISIBLE_KEY = <<<Same as github secret value>>>
            when running gradle daemon
            e.g. ./gradlew bootRun
                 >>> INVISIBLE_KEY=<<localmachine-secret-setting>> ./gradlew bootRun

            For running on GCP, an env global variable will be automatically set by using main.yml on .github/workflows
      */
      String secret="initialize";
      secret = System.getenv("INVISIBLE_KEY");
      System.out.println("LOCAL SECRET KEY CHECKPOINT " + secret);
      return "The application is running. " + secret;
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
      }
    };
  }
}
