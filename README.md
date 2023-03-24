# Petize - Desafio Técnico
###### Petize é um aplicativo que recebe pedidos de vendas de produtos e os processa. O objetivo deste desafio técnico é criar uma aplicação Java com o Spring Boot que utilize uma fila de mensagens para processar os pedidos recebidos.Petize é um aplicativo que recebe pedidos de vendas de produtos e os processa. O objetivo deste desafio técnico é criar uma aplicação Java com o Spring Boot que utilize uma fila de mensagens para processar os pedidos recebidos.
**A aplicação deve ser capaz de:**

- Receber pedidos de vendas via API REST;
- Salvar os pedidos no banco de dados MySQL;
- Enviar os dados dos pedidos para uma fila de mensagens RabbitMQ;
- Processar a fila de mensagens e atualizar o status dos pedidos no banco de dados.

**Pré-requisitos**

Antes de executar a aplicação, certifique-se de que possui as seguintes ferramentas instaladas em sua máquina:
- Docker
- Docker Compose
- Java Development Kit (JDK) 17

**Executando a aplicação**

Para executar a aplicação, siga os passos abaixo:
1. Clone este repositório em sua máquina:
```ruby
https://github.com/MarceloMacedoDev/petize.git
```

2. Inicie os containers Docker do MySQL e RabbitMQ e da aplicação:
 ```ruby
./mvnw clean package
docker-compose up --d
```
   Isso irá criar e iniciar os containers necessários para executar a aplicação.
   ◦ O container docker-mysql contém o banco de dados MySQL e será iniciado na porta 3306.
   ◦ O container docker-rabbitmq contém o RabbitMQ e será iniciado nas portas 5672 e 15672.
   ◦ O container java contém a aplicação e será iniciado na porta 8080.
### ***Tabela Product sera carregada com 20 produtos pre-cadastrados***

O aplicativo estará disponível em http://localhost:8080.
Endpoints

A API disponibiliza os seguintes endpoints:

**Para Order:**

- POST /orders - cria uma nova ordem com base nas informações fornecidas no corpo da solicitação (OrderDto)
- GET /orders - recupera todas as ordens
- GET /orders/{id} - recupera uma ordem com base no ID fornecido
- PUT /orders/{id} - atualiza uma ordem existente com base no ID fornecido e nas informações fornecidas no corpo da solicitação (OrderDto)
- DELETE /orders/{id} - exclui uma ordem com base no ID fornecido
- POST /orders/alterstatus - atualiza o status de uma ordem com base nas informações fornecidas no corpo da solicitação (OrderCreatedEventDTO)

**Para Product:**

- POST /products - cria um novo produto com base nas informações fornecidas no corpo da solicitação (ProductDto)
- GET /products - recupera todos os produtos
- GET /products/{id} - recupera um produto com base no ID fornecido
- PUT /products/{id} - atualiza um produto existente com base no ID fornecido e nas informações fornecidas no corpo da solicitação (ProductDto)
- DELETE /products/{id} - exclui um produto com base no ID fornecido

### Collection Postman
Utilizar a colection **petize.postman_collection.json** do postman para as principais comando de endpoints.


**Tratamento de exceções**

A API utiliza a classe ResourceExceptionHandler para tratar exceções de forma centralizada. As exceções tratadas são:
- ResourceNotFoundException: lançada quando não é possível encontrar um recurso correspondente ao ID informado
-  MethodArgumentNotValidException: lançada quando há erros de validação nos dados informados na requisição

**Mapeamento de entidades**

O projeto utiliza a interface OrderMapper para mapear as entidades de Order para OrderDto e vice-versa.

**Serviços**
O projeto utiliza a classe OrderService para executar as operações de CRUD no banco de dados e gerenciamento de status de pedidos.

**Serviço de eventos**
O serviço de eventos é responsável por atualizar o status dos pedidos quando um evento de OrderCreatedEvent é publicado no RabbitMQ.
O serviço de eventos é implementado no pacote com.petize.events e consiste em duas classes:
- EventPublisher: Publica eventos de OrderCreatedEvent no RabbitMQ
- OrderCreatedEventListener: Ouve os eventos de OrderCreatedEvent do RabbitMQ e atualiza o status dos pedidos

Aqui estão as informações adicionais para ajudá-lo a entender como o serviço de eventos é implementado:

**EventPublisher**

A classe EventPublisher publica eventos de OrderCreatedEvent no RabbitMQ. O método send é usado para publicar um evento.

**OrderCreatedEventListener**

A classe OrderCreatedEventListener ouve eventos de OrderCreatedEvent no RabbitMQ e atualiza o status dos pedidos correspondentes no banco de dados. O método consume é usado para receber um evento.

**Configuração do RabbitMQ**

A configuração do RabbitMQ é feita no arquivo application.properties. Aqui estão as configurações disponíveis:

```ruby
spring.rabbitmq.host=${RABBITMQ_HOST:localhost}
spring.rabbitmq.port=${RABBITMQ_PORT:5672}
spring.rabbitmq.username=${RABBITMQ_USERNAME:admin}
spring.rabbitmq.password=${RABBITMQ_PASSWORD:123456}
petize.rabbitmq.exchange=${RABBITMQ_EXCHANGE:petize.exchange}
petize.rabbitmq.queue=${RABBITMQ_QUEUE:petize.queue}
petize.rabbitmq.routingkey=${RABBITMQ_ROUTINGKEY:petize.routingkey}
```
# **Ambientes**
**Desenvolvimento**

O ambiente de desenvolvimento utiliza o banco de dados em memória H2. Para executar a aplicação nesse ambiente:src/main/resources/application-dev.properties.

**Testes**

O ambiente de testes utiliza o banco de dados MySQL. Para executar a aplicação nesse ambiente, basta definir a variável de ambiente APP_PROFILE como test.

Lembrando que é necessário ter as configurações de conexão com o banco de dados MySQL definidas no arquivo src/main/resources/application-test.properties.