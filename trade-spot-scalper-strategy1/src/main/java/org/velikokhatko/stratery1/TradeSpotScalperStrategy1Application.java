package org.velikokhatko.stratery1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class TradeSpotScalperStrategy1Application {

    public static void main(String[] args) {
        SpringApplication.run(TradeSpotScalperStrategy1Application.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
    }
}
