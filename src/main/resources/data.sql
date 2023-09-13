INSERT INTO usuario (ativado, cpf, nome, role, senha_criptografada)
VALUES(TRUE, '12345678911','Allan Toledo', 'TECNICO','$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe')
ON CONFLICT DO NOTHING;

INSERT INTO usuario (ativado, cpf, nome, role, senha_criptografada)
VALUES(TRUE, '98765432199','Allan Toledo', 'GESTOR','$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe')
ON CONFLICT DO NOTHING;
