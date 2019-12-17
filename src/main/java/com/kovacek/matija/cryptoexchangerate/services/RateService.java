package com.kovacek.matija.cryptoexchangerate.services;

import com.kovacek.matija.cryptoexchangerate.models.RateModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RateService {

    ResponseEntity<RateModel> getLatestRate(String currencyCode);

    ResponseEntity<List<RateModel>> getHistoricalRate(String currencyCode, String startDate, String endDate);

}