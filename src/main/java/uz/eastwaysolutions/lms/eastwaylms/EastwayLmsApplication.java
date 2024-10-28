package uz.eastwaysolutions.lms.eastwaylms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class EastwayLmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EastwayLmsApplication.class, args);
    }

}
