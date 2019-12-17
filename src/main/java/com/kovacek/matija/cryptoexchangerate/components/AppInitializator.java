package com.kovacek.matija.cryptoexchangerate.components;

import com.kovacek.matija.cryptoexchangerate.services.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AppInitializator {

    @Autowired
    private ImportService importService;

    @PostConstruct
    private void init() {
        importService.importCryptoCurrency("BTC", "Bitcoin");
        importService.importHistoricalSampleData("BTC");
    }
}
