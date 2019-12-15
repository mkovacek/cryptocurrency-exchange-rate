package com.kovacek.matija.cryptoexchangerate.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovacek.matija.cryptoexchangerate.models.CryptoCurrencyModel;
import com.kovacek.matija.cryptoexchangerate.models.RateModel;
import com.kovacek.matija.cryptoexchangerate.repositories.CryptoCurrencyRepository;
import com.kovacek.matija.cryptoexchangerate.repositories.RateRepository;
import com.kovacek.matija.cryptoexchangerate.services.ImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class ImportServiceImpl implements ImportService {

    private static final Logger log = LoggerFactory.getLogger(ImportServiceImpl.class);

    private static final String COINDESK_HISTORICAL_API = "https://api.coindesk.com/v1/bpi/historical/close.json";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    @Autowired
    private RateRepository rateRepository;

    @Override
    public void importCryptoCurrency(String code, String name) {
        CryptoCurrencyModel cryptoCurrencyModel = new CryptoCurrencyModel(code, name);
        cryptoCurrencyRepository.save(cryptoCurrencyModel);
    }

    @Override
    public void importHistoricalSampleData(String code) {
        Optional<CryptoCurrencyModel> cryptoCurrencyModel = cryptoCurrencyRepository.findByCodeIgnoreCase(code);
        cryptoCurrencyModel.ifPresent(cryptoCurrency -> {
            getRates().ifPresent(rates -> saveRates(cryptoCurrency, rates));
        });
    }

    private Optional<JsonNode> getRates() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForEntity(COINDESK_HISTORICAL_API, String.class).getBody();
            return Optional.of(mapper.readTree(response).get("bpi"));
        } catch (JsonProcessingException e) {
            log.error("Error during importing historical rate ", e);
        }
        return Optional.empty();
    }

    private void saveRates(CryptoCurrencyModel cryptoCurrencyModel, JsonNode rates) {
        rates.fields().forEachRemaining(field -> {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(field.getKey());
                RateModel rateModel = new RateModel(date, cryptoCurrencyModel, field.getValue().toString());
                rateRepository.save(rateModel);
            } catch (ParseException e) {
                log.error("Error during parsing ", e);
            }
        });
    }

}