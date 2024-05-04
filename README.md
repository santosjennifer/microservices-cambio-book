![Last Commit](https://img.shields.io/github/last-commit/santosjennifer/rest-microservice)
[![Continuous Integration with Github](https://github.com/santosjennifer/rest-microservice/actions/workflows/docker-publish.yml/badge.svg)](https://github.com/santosjennifer/rest-microservice/actions/workflows/docker-publish.yml)


### Repositório com os microserviços ms-eureka-server, ms-gateway, ms-cambio e ms-book.

- **ms-eureka-server:** responsável por registrar e gerenciar a comunicação dos demais microserviços (ms-gateway, ms-cambio e ms-book).
- **ms-gateway:** responsável gerenciar as rotas do API Gateway.
- **ms-cambio:** responsável por retornar o valor do cambio de acordo com a moeda.
- **ms-book:** responsável por retornar os dados de um livro e seu valor retornando pelo cambio.


### Tecnologia

- Java 21
- Maven
- Spring Boot 3.2.3

### Swagger

- ms-cambio - [Swagger local](http://localhost:8000/swagger-ui/index.html):
![image](https://github.com/santosjennifer/rest-microservice/assets/90192611/c98b55b4-ad97-4410-ace5-e407cfd1b54b)

- ms-book - [Swagger local](http://localhost:8100/swagger-ui/index.html):
![image](https://github.com/santosjennifer/rest-microservice/assets/90192611/b54bb04b-ccda-4752-a25d-fa9f1ef2ebcf)

### API

**GET Cambio:**

Request:
```
curl --location 'http://localhost:8000/cambio/55.0/USD/BRL'
```
Response:
```
{
    "id": 1,
    "from": "USD",
    "to": "BRL",
    "conversionFactor": 5.73,
    "convertedValue": 315.15,
    "environment": "8000"
}
```

**GET Book:**

Request:
```
curl --location 'http://localhost:8100/book/2/BRL'
```
Response:
```
{
    "id": 2,
    "title": "Design Patterns",
    "author": "Ralph Johnson, Erich Gamma, John Vlissides e Richard Helm",
    "launchDate": "2017-11-29T15:15:13.636+00:00",
    "price": 45.1,
    "environment": "Book port 8100 Cambio port 8000"
}
```
**GET Cambio via API Gateway:**

Request:
```
curl --location 'http://localhost:8765/cambio/44/USD/BRL'
```
Response:
```
{
    "id": 1,
    "from": "USD",
    "to": "BRL",
    "conversionFactor": 5.73,
    "convertedValue": 252.12,
    "environment": "8000"
}
```

**GET Book via API Gateway:**

Request:
```
curl --location 'http://localhost:8765/book/4/BRL'
```
Response:
```
{
    "id": 4,
    "title": "JavaScript",
    "author": "Crockford",
    "launchDate": "2017-11-07T15:09:01.674+00:00",
    "price": 67.1,
    "environment": "Book port 8100 Cambio port 8000"
}
```

### Eureka Server:
![image](https://github.com/santosjennifer/rest-microservice/assets/90192611/ba69625d-bcbe-4ace-b77c-9c6ed90e7e6a)
