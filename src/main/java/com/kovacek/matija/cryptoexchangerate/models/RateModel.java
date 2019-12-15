package com.kovacek.matija.cryptoexchangerate.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class RateModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(unique = true)
    private Date date;

    private String price;

    @ManyToOne
    @JoinColumn
    private CryptoCurrencyModel cryptoCurrency;

    public RateModel() {
    }

    public RateModel(final Date date, final CryptoCurrencyModel cryptoCurrency, final String price) {
        this.date = date;
        this.cryptoCurrency = cryptoCurrency;
        this.price = price;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(final String price) {
        this.price = price;
    }

    public String getCryptoCurrencyName() {
        return cryptoCurrency.getName();
    }

    public String getCryptoCurrencyCode() {
        return cryptoCurrency.getCode();
    }

    @Override
    public String toString() {
        return "RateModel{" +
                "id=" + id +
                ", date=" + date +
                ", price=" + price +
                ", cryptoCurrency=" + cryptoCurrency +
                '}';
    }
}