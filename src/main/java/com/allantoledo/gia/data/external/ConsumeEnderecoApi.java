package com.allantoledo.gia.data.external;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumeEnderecoApi {

    RestTemplate restTemplate;

    public ConsumeEnderecoApi(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public EnderecoApi loadCep(String cep) {
        try {
            return restTemplate.getForObject("https://viacep.com.br/ws/" + cep + "/json", EnderecoApi.class);
        } catch(RestClientException e) {
            return EnderecoApi.EMPTY;
        }
    }
}
