package com.microservices.apigateway.configuration;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;


@Configuration
public class ApiGatewayConfiguration {

    //Если приходит запрос на "/get" мы перенаправляем его на "http://httpbin.org:80"
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder){
        return builder.routes().route(predicateSpec -> predicateSpec.path("/get")
                .filters(f ->
                        f.addRequestHeader("MyHeader", "MyURI"))
                        .uri("http://httpbin.org:80"))
/*если URL-адрес запроса начинается с currency exchange что бы я хотел сделать, так этоперенаправьте их с помощью сервера именования.
И я бы также хотел заняться балансировкой нагрузки.
Как я могу это сделать?
Это можно сделать следующим образом
просто введя lb://
и имя для регистрации на сервере eureka.*/
                .route(predicateSpec -> predicateSpec.path("/currency-exchange/**")
                        .uri("lb://currency-exchange"))
                .route(predicateSpec -> predicateSpec.path("/currency-conversion/**")
                        .uri("lb://currency-conversion/**"))
                .route(predicateSpec -> predicateSpec.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion"))
                .route(predicateSpec -> predicateSpec.path("/currency-conversion-new/**")
                        /*Этот путь перезаписи имеет два аргумента, которые необходимо
                        необходимо пройти.
                        Первый вопрос - какую строку следует заменить
                        второй чем заменить*/
                        .filters(f -> f.rewritePath(
                                "currency-conversion-new/(?<segment>.*)",
                                /*что бы ни последовало за конвертацией валюты, новое,
                                Я бы хотел добавить его в*/
                                "currency-conversion-feign/${segment}"
                        ))
                        .uri("lb://currency-conversion"))
                .build();
    }
}
