package veil.hdp.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeneratorApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(GeneratorApplication.class);

        app.setWebEnvironment(false);

        app.run(args);
    }

}
