package com.allantoledo.gia.data.external;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumeEnderecoApi {

    final RestTemplate restTemplate;

    public ConsumeEnderecoApi(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public EnderecoApi loadCep(String cep) {
        try {
            var response = restTemplate.getForObject("https://viacep.com.br/ws/" + cep + "/json", EnderecoApi.class);
            assert(response != null);
            assert response.cep() != null;
            assert response.logradouro() != null;
            assert response.bairro() != null;
            assert response.localidade() != null;
            assert response.uf() != null;
            return response;
        } catch(RestClientException e) {
            return EnderecoApi.EMPTY;
        }
    }
}
