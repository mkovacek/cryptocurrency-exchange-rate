package com.kovacek.matija.cryptoexchangerate.services.impl;

import com.kovacek.matija.cryptoexchangerate.models.CryptoCurrencyModel;
import com.kovacek.matija.cryptoexchangerate.models.RateModel;
import com.kovacek.matija.cryptoexchangerate.repositories.CryptoCurrencyRepository;
import com.kovacek.matija.cryptoexchangerate.repositories.RateRepository;
import com.kovacek.matija.cryptoexchangerate.services.RateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RateServiceImpl implements RateService {

    private static final Logger log = LoggerFactory.getLogger(RateServiceImpl.class);

    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    @Autowired
    private RateRepository rateRepository;

    @Override
    public ResponseEntity<RateModel> getLatestRate(String currencyCode) {
        Optional<CryptoCurrencyModel> cryptoCurrencyModel = cryptoCurrencyRepository.findByCodeIgnoreCase(currencyCode);
        return cryptoCurrencyModel
                .map(cryptoCurrency -> new ResponseEntity<>(rateRepository.findTopByCryptoCurrencyOrderByIdDesc(cryptoCurrency), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @Override
    public ResponseEntity<List<RateModel>> getHistoricalRate(String currencyCode, String startDate, String endDate) {
        try {
            Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            Optional<CryptoCurrencyModel> cryptoCurrencyModel = cryptoCurrencyRepository.findByCodeIgnoreCase(currencyCode);
            return cryptoCurrencyModel
                    .map(cryptoCurrency -> new ResponseEntity<>(rateRepository.findByCryptoCurrencyAndDateGreaterThanEqualAndDateLessThanEqual(cryptoCurrency, start, end), HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } catch (ParseException e) {
            log.error("Error during parsing dates ", e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}