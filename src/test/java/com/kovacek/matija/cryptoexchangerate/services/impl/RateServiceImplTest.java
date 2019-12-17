package com.kovacek.matija.cryptoexchangerate.services.impl;

import com.kovacek.matija.cryptoexchangerate.models.CryptoCurrencyModel;
import com.kovacek.matija.cryptoexchangerate.models.RateModel;
import com.kovacek.matija.cryptoexchangerate.repositories.CryptoCurrencyRepository;
import com.kovacek.matija.cryptoexchangerate.repositories.RateRepository;
import com.kovacek.matija.cryptoexchangerate.services.RateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RateServiceImplTest {

    @TestConfiguration
    static class RateServiceImplTestContextConfiguration {

        @Bean
        public RateService rateService() {
            return new RateServiceImpl();
        }

    }

    @Autowired
    private RateService rateService;

    @MockBean
    private RateRepository rateRepository;

    @MockBean
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    @Test
    @DisplayName("Given not supported currency code when latest rate is requested then service should return bad request response entity")
    void getLatestRateForNonSupportedCurrency() {
        when(cryptoCurrencyRepository.findByCodeIgnoreCase(anyString())).thenReturn(Optional.empty());
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), rateService.getLatestRate("eth"));
    }

    @Test
    @DisplayName("Given currency code when latest rate is requested then service should return response entity with rate model")
    void getLatestRateFoSupportedCurrency() throws ParseException {
        CryptoCurrencyModel cryptoCurrencyModel = getBtc();
        RateModel rateModel = new RateModel(getDate("2019-12-17"), cryptoCurrencyModel, "6.869,19");
        when(cryptoCurrencyRepository.findByCodeIgnoreCase(cryptoCurrencyModel.getCode())).thenReturn(Optional.of(cryptoCurrencyModel));
        when(rateRepository.findTopByCryptoCurrencyOrderByIdDesc(cryptoCurrencyModel)).thenReturn(rateModel);

        assertEquals(new ResponseEntity<>(rateModel, HttpStatus.OK), rateService.getLatestRate(cryptoCurrencyModel.getCode()));
    }

    @Test
    @DisplayName("Given not supported currency code when historical rate is requested then service should return bad request response entity")
    void getHistoricalRateForNonSupportedCurrency() {
        when(cryptoCurrencyRepository.findByCodeIgnoreCase(anyString())).thenReturn(Optional.empty());
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), rateService.getHistoricalRate("eth", "2019-12-10", "2019-12-12"));
    }

    @Test
    @DisplayName("Given not valid date format when historical rate is requested then service should return bad request response entity")
    void getHistoricalRateForNotValidDateFormat() {
        when(cryptoCurrencyRepository.findByCodeIgnoreCase(anyString())).thenReturn(Optional.empty());
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), rateService.getHistoricalRate("btc", "10-12-2019", "12-12-2019"));
    }

    @Test
    @DisplayName("Given supported currency code when historical rate is requested then service should return response entity with list of rate models")
    void getHistoricalRateForSupportedCurrency() throws ParseException {
        CryptoCurrencyModel cryptoCurrencyModel = getBtc();
        RateModel rateModel = new RateModel(getDate("2019-12-16"), cryptoCurrencyModel, "6.869,19");
        RateModel rateModel2 = new RateModel(getDate("2019-12-17"), cryptoCurrencyModel, "6.854,22");
        List<RateModel> rateModelList = List.of(rateModel, rateModel2);

        when(cryptoCurrencyRepository.findByCodeIgnoreCase(cryptoCurrencyModel.getCode())).thenReturn(Optional.of(cryptoCurrencyModel));
        when(rateRepository.findByCryptoCurrencyAndDateGreaterThanEqualAndDateLessThanEqual(cryptoCurrencyModel, getDate("2019-12-16"), getDate("2019-12-17"))).thenReturn(rateModelList);

        assertEquals(new ResponseEntity<>(rateModelList, HttpStatus.OK), rateService.getHistoricalRate("btc", "2019-12-16", "2019-12-17"));
    }

    private CryptoCurrencyModel getBtc() {
        return new CryptoCurrencyModel("btc", "Bitcoin");
    }

    private Date getDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

}