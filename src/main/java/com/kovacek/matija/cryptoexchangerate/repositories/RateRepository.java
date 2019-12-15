package com.kovacek.matija.cryptoexchangerate.repositories;

import com.kovacek.matija.cryptoexchangerate.models.CryptoCurrencyModel;
import com.kovacek.matija.cryptoexchangerate.models.RateModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RateRepository extends CrudRepository<RateModel, Long> {
    List<RateModel> findByCryptoCurrencyAndDateGreaterThanEqualAndDateLessThanEqual(CryptoCurrencyModel cryptoCurrencyModel, Date startDate, Date endDate);

    Optional<RateModel> findByCryptoCurrencyAndDate(CryptoCurrencyModel cryptoCurrencyModel, Date date);

    RateModel findTopByCryptoCurrencyOrderByIdDesc(CryptoCurrencyModel cryptoCurrencyModel);
}
