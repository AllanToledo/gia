package com.allantoledo.gia.data.external;

public record EnderecoApi(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        String ibge,
        String gia,
        String ddd,
        String siafi
){
    public static final EnderecoApi EMPTY = new EnderecoApi("","","","","","","","","","");
}
