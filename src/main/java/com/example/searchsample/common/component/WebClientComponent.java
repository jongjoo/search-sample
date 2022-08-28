package com.example.searchsample.common.component;

import com.example.searchsample.common.dto.RequestRecord;
import com.example.searchsample.common.dto.ResponseRecord;
import com.example.searchsample.common.utils.JsonUtils;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class WebClientComponent {

    public <T> Mono<ResponseRecord> sendRequest(RequestRecord<T> requestRecord, HttpMethod httpMethod) {
        WebClient webClient = builderWebClient(requestRecord.baseUrl());
        WebClient.RequestBodySpec requestBodySpec = builderBodySpec(webClient, httpMethod, requestRecord);
        String body = JsonUtils.toString(requestRecord.payload());
        return doExchangeMono(httpMethod, body, requestBodySpec);
    }

    private Mono<ResponseRecord> doExchangeMono(HttpMethod httpMethod, String body, WebClient.RequestBodySpec requestSpec) {
        return switch (httpMethod) {
            case POST, PUT, PATCH -> requestSpec.bodyValue(body).exchangeToMono(this::doResponseAction);
            default -> requestSpec.exchangeToMono(this::doResponseAction);
        };
    }

    private Mono<ResponseRecord> doResponseAction(ClientResponse clientResponse) {
        HttpStatus httpStatus = clientResponse.statusCode();
        HttpHeaders headers = clientResponse.headers().asHttpHeaders();
        if (httpStatus.isError()) {
            log.error("doResponseAction.error {} ", clientResponse);
        }
        return clientResponse.bodyToMono(String.class).map(body -> {
            String toBody = String.valueOf(body);
            return new ResponseRecord(httpStatus
                    , headers
                    , toBody);
        });
    }

    public WebClient builderWebClient(String baseUrl) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(builderWebClientTime()))
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private HttpClient builderWebClientTime() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(5))
                                .addHandlerLast(new WriteTimeoutHandler(5))
                )
                .responseTimeout(Duration.ofSeconds(5));
    }

    private <T> WebClient.RequestBodySpec builderBodySpec(WebClient webClient, HttpMethod httpMethod, RequestRecord<T> requestRecord) {
        Map<String, String> headers = requestRecord.headers();
        Set<String> headerKeys = headers.keySet();
        Iterator<String> iteratorKeys = headerKeys.iterator();
        return webClient.method(httpMethod)
                .uri(requestRecord.uri())
                .headers(httpHeaders -> {
                    while (iteratorKeys.hasNext()) {
                        String key = iteratorKeys.next();
                        httpHeaders.add(key, headers.get(key));
                    }
                })
                .accept(MediaType.APPLICATION_JSON);
    }

}
