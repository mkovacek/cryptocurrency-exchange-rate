package com.kovacek.matija.cryptoexchangerate.repositories;

import com.kovacek.matija.cryptoexchangerate.models.CryptoCurrencyModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CryptoCurrencyRepository extends CrudRepository<CryptoCurrencyModel, Long> {

    Optional<CryptoCurrencyModel> findByCodeIgnoreCase(String code);

    Optional<CryptoCurrencyModel> findByNameIgnoreCase(String name);

}