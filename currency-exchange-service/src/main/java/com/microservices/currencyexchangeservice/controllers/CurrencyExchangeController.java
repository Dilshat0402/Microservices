package com.microservices.currencyexchangeservice.controllers;

import com.microservices.currencyexchangeservice.models.CurrencyExchange;
import com.microservices.currencyexchangeservice.repositories.CurrencyExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyExchangeController {

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;
    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange getExchangeValue(
            @PathVariable String from,
            @PathVariable String to){
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from,to);
        if (currencyExchange==null){
            throw new RuntimeException("Unable to Find data for " + from + " to " + to);
        }
        String port = environment.getProperty("local.server.port");
        currencyExchange.setEnvironment(port);
        return currencyExchange;
    }
}
