package com.kovacek.matija.cryptoexchangerate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptocurrencyExchangeRateApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptocurrencyExchangeRateApplication.class, args);
    }
}
