package br.com.db.sicredi.votos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class VotosApplication {

    public static void main(String[] args) {
        SpringApplication.run(VotosApplication.class, args);
    }
}