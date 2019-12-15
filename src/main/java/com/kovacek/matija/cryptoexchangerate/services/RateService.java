package com.kovacek.matija.cryptoexchangerate.services;

import com.kovacek.matija.cryptoexchangerate.models.RateModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RateService {
    ResponseEntity<RateModel> getLatestRate(String currencyName);

    ResponseEntity<List<RateModel>> getHistoricalRate(String currencyName, String startDate, String endDate);
}
