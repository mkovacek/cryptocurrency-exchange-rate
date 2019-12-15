package com.kovacek.matija.cryptoexchangerate.services;

public interface ImportService {

    void importCryptoCurrency(String code, String name);

    void importHistoricalSampleData(String code);

}