# spring-reactive-api

## Executando imagem Docker

Nesse projeto também temos o [Dockerfile](./Dockerfile) que pode gerar uma
imagem do projeto e executada, assim você pode subir containers com essa
aplicação!

Para isso você pode buildar a imagem do projeto usando o seguinte comando:
````shell
docker build -t spring-reactive-api .
````

Isso irá gerar uma imagem do projeto que pode ser executada a qualquer momento
pelo docker.
E para rodar essa imagem execute o seguinte comando:
````shell
docker run -p 8081:8081 spring-reactive-api:latest
````
O argumento `-p 8081:8081` vai expor a porta 8080 no seu localhost e assim você
poderá realizar requisições direto para a máquina.

> Porém, a aplicação pode não rodar por não conseguir acesso ao banco de dados.
