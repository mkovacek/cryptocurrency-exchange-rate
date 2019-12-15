package com.kovacek.matija.cryptoexchangerate.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class CryptoCurrencyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    private String name;

    @OneToMany(mappedBy = "cryptoCurrency", cascade = CascadeType.ALL)
    private List<RateModel> rate;

    public CryptoCurrencyModel() {
    }

    public CryptoCurrencyModel(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<RateModel> getRate() {
        return this.rate;
    }
}
