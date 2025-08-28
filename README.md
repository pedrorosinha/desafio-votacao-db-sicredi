# Sistema de Votação Cooperativa

Este projeto é uma API REST para gerenciar pautas, sessões de votação e votos dos associados em uma cooperativa.

## Configuração

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/pedrorosinha/desafio-votacao-db-sicredi.git
   cd votos
   ```

2. **Configuração do banco de dados:**
   - Por padrão, utiliza H2 em memória/persistente. Para alterar, edite `src/main/resources/application.properties`:
     ```
     spring.datasource.url=jdbc:h2:file:./data/votosdb
     spring.datasource.username=sa
     spring.datasource.password=
     spring.jpa.hibernate.ddl-auto=update
     ```
   - Para usar outro banco (ex: PostgreSQL), ajuste as propriedades acima.

## Execução

Para rodar a aplicação localmente executa o arquivo:

```bash
VotosApplication.java
```

## Testes

Para rodar os testes automatizados:

```bash
mvn test
```

<img width="811" height="368" alt="image" src="https://github.com/user-attachments/assets/feaf4524-da64-4b26-9a7d-86cc0e8a40fc" />
