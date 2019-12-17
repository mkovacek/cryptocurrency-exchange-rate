package com.kovacek.matija.cryptoexchangerate.controllers;

import com.kovacek.matija.cryptoexchangerate.models.CryptoCurrencyModel;
import com.kovacek.matija.cryptoexchangerate.models.RateModel;
import com.kovacek.matija.cryptoexchangerate.services.RateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RateController.class)
class RateControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RateService rateService;

    @Test
    @DisplayName("Given BTC rate model when latest rate is requested then API should return latest BTC rate")
    void testGetLatestRateForSupportedCurrency() throws Exception {
        RateModel rateModel = new RateModel(getDate("2019-12-17"), getBtc(), "6.869,19");

        given(rateService.getLatestRate("btc")).willReturn(new ResponseEntity<>(rateModel, HttpStatus.OK));

        mvc.perform(get("/api/v1/cryptocurrency/rate/latest/btc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cryptoCurrencyName", is("Bitcoin")))
                .andExpect(jsonPath("$.cryptoCurrencyCode", is("BTC")))
                .andExpect(jsonPath("$.price", is("6.869,19")));
    }

    @Test
    @DisplayName("Given non supported ETH rate model when latest rate is requested then API should return empty bad request")
    void testGetLatestRateForNonSupportedCurrency() throws Exception {
        given(rateService.getLatestRate("eth")).willReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        mvc.perform(get("/api/v1/cryptocurrency/rate/latest/eth")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given currency code, start and end date when historical rate is requested then API should return historical rate")
    void getHistoricalRateForSupportedCurrency() throws Exception {
        CryptoCurrencyModel cryptoCurrencyModel = getBtc();
        RateModel rateModel = new RateModel(getDate("2019-12-08"), cryptoCurrencyModel, "6.869,19");
        RateModel rateModel2 = new RateModel(getDate("2019-12-09"), cryptoCurrencyModel, "6.923,23");

        List<RateModel> rateModels = List.of(rateModel, rateModel2);

        given(rateService.getHistoricalRate("btc", "2019-12-08", "2019-12-09")).willReturn(new ResponseEntity<>(rateModels, HttpStatus.OK));

        mvc.perform(get("/api/v1/cryptocurrency/rate/historical/btc?startDate=2019-12-08&endDate=2019-12-09")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price", is("6.869,19")))
                .andExpect(jsonPath("$[1].price", is("6.923,23")));
    }

    @Test
    @DisplayName("Given currency code and not valid start and end date when historical rate is requested then API should return empty array")
    void getHistoricalRateForSupportedCurrencyAndNotValidDate() throws Exception {
        given(rateService.getHistoricalRate("btc", "2020-12-08", "2020-12-09")).willReturn(new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK));

        mvc.perform(get("/api/v1/cryptocurrency/rate/historical/btc?startDate=2020-12-08&endDate=2020-12-09")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("Given non supported currency, start and end date when historical rate is requested then API should return empty bad request")
    void getHistoricalRateForNonSupportedCurrency() throws Exception {
        given(rateService.getHistoricalRate("eth", "2019-11-16", "2019-12-10")).willReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        mvc.perform(get("/api/v1/cryptocurrency/rate/historical/eth?startDate=2019-11-16&endDate=2019-12-10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private CryptoCurrencyModel getBtc() {
        return new CryptoCurrencyModel("BTC", "Bitcoin");
    }

    private Date getDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }
}