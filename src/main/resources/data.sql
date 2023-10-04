INSERT INTO usuario (ativado, cpf, nome, role, senha_criptografada)
VALUES (TRUE, '12312312312', 'ADMIN', 'GESTOR', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe'),
       (TRUE, '41668403048', 'ALLAN TOLEDO', 'TECNICO', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe'),
       (TRUE, '54758441006', 'CARLOS RAFAEL', 'TECNICO',
        '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe'),
       (TRUE, '82864168057', 'ANTONIO BANDEIRAS', 'TECNICO',
        '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe'),
       (FALSE, '77398436017', 'GUILHERME COELHO', 'TECNICO',
        '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe'),
       (FALSE, '39881313007', 'JOÃO PADILHA', 'TECNICO', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe'),
       (TRUE, '25608552083', 'ROGER FUJII', 'GESTOR', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe'),
       (TRUE, '64351720050', 'MARCOS SILVA', 'GESTOR', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe')
ON CONFLICT DO NOTHING;

INSERT INTO categoria_item(nome_categoria)
VALUES ('MOTOCICLETA'),
       ('CARRO'),
       ('DIPOSITIVO ELETRONICO'),
       ('MOEDA'),
       ('AERONAVES'),
       ('ANIMAIS'),
       ('ROUPAS'),
       ('ALIMENTOS PERECIVEIS'),
       ('ALIMENTOS'),
       ('FERRAMENTAS'),
       ('ARMA DE FOGO'),
       ('ARMA BRANCA');

INSERT INTO classe_processo(nome_classe)
VALUES ('TRAFICO'),
       ('CONTRABANDO'),
       ('FURTO'),
       ('ROUBO'),
       ('CRIMES AMBIENTAIS'),
       ('FALSIFICACAO');

INSERT INTO orgao_apreensor(nome)
VALUES ('POLICIA CIVIL'),
       ('POLICIA MILITAR'),
       ('POLICIA RODOVIARIA FEDERAL'),
       ('POLICIA FEDERAL'),
       ('IBAMA');




