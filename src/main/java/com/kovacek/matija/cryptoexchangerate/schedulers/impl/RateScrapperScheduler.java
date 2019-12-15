package com.kovacek.matija.cryptoexchangerate.schedulers.impl;

import com.kovacek.matija.cryptoexchangerate.models.CryptoCurrencyModel;
import com.kovacek.matija.cryptoexchangerate.models.RateModel;
import com.kovacek.matija.cryptoexchangerate.repositories.CryptoCurrencyRepository;
import com.kovacek.matija.cryptoexchangerate.repositories.RateRepository;
import com.kovacek.matija.cryptoexchangerate.schedulers.RateImport;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class RateScrapperScheduler implements RateImport {

    private static final Logger log = LoggerFactory.getLogger(RateScrapperScheduler.class);

    private static final String URL = "https://coinmarketcap.com/currencies/";
    private static final String ELEMENT_WITH_LATEST_PRICE_CSS_CLASS = "cmc-details-panel-price__price";

    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    @Autowired
    private RateRepository rateRepository;

    @Override
    @Scheduled(cron = "${checkPeriod}")
    public void saveLatestPrice() {
        try {
            Document document = Jsoup.connect(StringUtils.join(URL, "/bitcoin")).get();
            Elements elements = document.getElementsByClass(ELEMENT_WITH_LATEST_PRICE_CSS_CLASS);
            String price = elements.text().substring(1);

            Optional<CryptoCurrencyModel> cryptoCurrencyModel = cryptoCurrencyRepository.findByNameIgnoreCase("bitcoin");
            cryptoCurrencyModel
                    .flatMap(cryptoCurrency -> rateRepository.findByCryptoCurrencyAndDate(cryptoCurrency, new Date()))
                    .ifPresentOrElse(rate -> updatePrice(rate, price), () -> savePrice(cryptoCurrencyModel.get(), price));

        } catch (IOException e) {
            log.error("Error during saving latest price: ", e);
        }
    }

    private void updatePrice(RateModel rateModel, String price) {
        rateModel.setPrice(price);
        rateRepository.save(rateModel);
    }

    private void savePrice(CryptoCurrencyModel cryptoCurrencyModel, String price) {
        rateRepository.save(new RateModel(new Date(), cryptoCurrencyModel, price));
    }
}