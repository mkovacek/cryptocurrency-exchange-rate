package com.kovacek.matija.cryptoexchangerate;

import com.kovacek.matija.cryptoexchangerate.services.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptocurrencyExchangeRateApplication implements CommandLineRunner {
    @Autowired
    private ImportService importService;

    public static void main(String[] args) {
        SpringApplication.run(CryptocurrencyExchangeRateApplication.class, args);
    }

    @Override
    public void run(String... args) {
        importService.importCryptoCurrency("BTC", "Bitcoin");
        importService.importHistoricalSampleData("BTC");
    }
}
