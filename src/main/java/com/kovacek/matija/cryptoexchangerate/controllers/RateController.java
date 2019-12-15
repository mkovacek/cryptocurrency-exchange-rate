package com.kovacek.matija.cryptoexchangerate.controllers;

import com.kovacek.matija.cryptoexchangerate.models.RateModel;
import com.kovacek.matija.cryptoexchangerate.services.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cryptocurrency/rate")
public class RateController {
    @Autowired
    private RateService rateService;

    @RequestMapping(value = "latest/{currencyCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RateModel> getLatestRate(@PathVariable("currencyCode") String currencyCode) {
        return rateService.getLatestRate(currencyCode);
    }

    @RequestMapping(value = "historical/{currencyCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RateModel>> getHistoricalRate(@PathVariable("currencyCode") String currencyCode, @RequestParam String startDate, @RequestParam String endDate) {
        return rateService.getHistoricalRate(currencyCode, startDate, endDate);
    }
}
