package org.velikokhatko.stratery1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.velikokhatko.re")
public class TradeSpotScalperStrategy1Application {

    public static void main(String[] args) {
        SpringApplication.run(TradeSpotScalperStrategy1Application.class, args);
    }
}
