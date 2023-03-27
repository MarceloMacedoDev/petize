# Petize - Desafio Técnico
 **Petize é um aplicativo que recebe pedidos de vendas de produtos e os processa. O objetivo deste desafio técnico é criar uma aplicação Java com o Spring Boot que utilize uma fila de mensagens para processar os pedidos recebidos.Petize é um aplicativo que recebe pedidos de vendas de produtos e os processa. O objetivo deste desafio técnico é criar uma aplicação Java com o Spring Boot que utilize uma fila de mensagens para processar os pedidos recebidos.**
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

**Endpoints**

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

O projeto utiliza a interface OrderMapper para mapear as entidades de Order para OrderDto e de Product para ProductDto e vice-versa.

**Serviços**

O projeto utiliza a classe OrderService para executar as operações de CRUD no banco de dados e gerenciamento de status de pedidos.

**Serviço de eventos**

O serviço de eventos é responsável por atualizar o status dos pedidos quando um evento de OrderCreatedEvent é publicado no RabbitMQ.
O serviço de eventos é implementado no pacote br.com.petize.aplication.events e consiste em dois mótodos:
- eventPublisher: Publica eventos de OrderCreatedEvent no RabbitMQ
- orderCreatedEventListener: Ouve os eventos de OrderCreatedEvent do RabbitMQ e atualiza o status dos pedidos

### Configuração do RabbitMQ

Esta aplicação utiliza o RabbitMQ para comunicação entre seus módulos. A configuração do RabbitMQ é definida na classe `RabbitMQConfig`.

**Parâmetros**

Os seguintes parâmetros são utilizados na classe `RabbitMQConfig`:

- **`petize.rabbitmq.queue`: nome da fila.
- `petize.rabbitmq.exchange`: nome da troca.
- `petize.rabbitmq.routingkey`: rota para a troca.

**Métodos**

A classe `RabbitMQConfig` possui os seguintes métodos:

- `queue()`: cria uma fila durável, exclusiva e auto-excluível no RabbitMQ.
- `exchange()`: cria uma troca direta no RabbitMQ.
- `binding(Queue queue, DirectExchange exchange)`: cria uma ligação entre a fila e a troca definidas.
- `jsonMessageConverter()`: define o conversor de mensagens para JSON a ser utilizado pelo RabbitMQ.
- `rabbitTemplate(ConnectionFactory connectionFactory)`: retorna um objeto AmqpTemplate configurado com o RabbitTemplate e o MessageConverter.

Para mais informações, consulte a documentação do [RabbitMQ](https://www.rabbitmq.com/documentation.html).

```ruby
spring.rabbitmq.host=${RABBITMQ_HOST:localhost}
spring.rabbitmq.port=${RABBITMQ_PORT:5672}
spring.rabbitmq.username=${RABBITMQ_USERNAME:admin}
spring.rabbitmq.password=${RABBITMQ_PASSWORD:123456}
petize.rabbitmq.exchange=${RABBITMQ_EXCHANGE:petize.exchange}
petize.rabbitmq.queue=${RABBITMQ_QUEUE:petize.queue}
petize.rabbitmq.routingkey=${RABBITMQ_ROUTINGKEY:petize.routingkey}
```
### Testes Unitários

Foi produzido classes de testes Unitários para as Classes de Service,  Comtrolle e Components

## **Ambientes**

**Desenvolvimento**

O ambiente de desenvolvimento utiliza o banco de dados em memória H2. Para executar a aplicação nesse ambiente:src/main/resources/application-dev.properties.

**Testes**

O ambiente de testes utiliza o banco de dados MySQL. Para executar a aplicação nesse ambiente, basta definir a variável de ambiente APP_PROFILE como test.

Lembrando que é necessário ter as configurações de conexão com o banco de dados MySQL definidas no arquivo src/main/resources/application-test.properties.

## Resilienc4j - Retry
O Retry é utilizado para lidar com possíveis falhas temporárias na comunicação com o RabbitMQ. O componente RetryComponent é responsável por criar um objeto RetryRegistry, que guarda as configurações de retry, e por executar um bloco de código com a opção de retry, através do método executeWithRetry.

Abaixo estão as configurações do retry:

- **maxAttempts**: número máximo de tentativas de comunicação antes de retornar erro;
- **waitDuration**: tempo de espera entre as tentativas de comunicação.

Essas configurações estão definidas no arquivo **application.properties**, na seção retry. Caso seja necessário alterá-las, basta atualizar os valores correspondentes nessa seção.

## Monitoramento - Prometheus

O Prometheus é utilizado para monitorar a aplicação e coletar métricas de performance e disponibilidade. A implementação das métricas é feita através do uso da biblioteca Micrometer, que possui integração com o Prometheus.

Abaixo estão os componentes que foram configurados para exportar métricas:

- Counters: utilizados para medir a quantidade de vezes que um evento ocorre. Foram configurados dois contadores:

**rabbitmq_sent_messages_total**: contador de mensagens enviadas para o RabbitMQ;
**rabbitmq_received_messages_total**: contador de mensagens recebidas do RabbitMQ.

- Timers: utilizados para medir o tempo de execução de um bloco de código. Foi configurado um timer:

**rabbitmq_event_send_duration_time**: mede o tempo de execução do método eventPublisher da classe RabbitMQComponent.
**rabbitmq_event_received_duration_time**: mede o tempo de execução recebimeto do RabbitMQ.

As métricas podem ser acessadas através da URL http://localhost:8080/actuator/prometheus. Além disso, o Prometheus pode ser configurado para fazer scraping dessas métricas, permitindo a visualização e análise dos dados através de gráficos e dashboards.

### Coletar métricas e logs na AWS, você pode seguir os seguintes passos:

- Configurar o envio de logs e métricas para o Amazon CloudWatch, usando ferramentas como AWS SDK, AWS CLI, AWS CloudFormation.

- Utilizar o Amazon CloudWatch para visualizar, analisar e armazenar os logs e métricas coletados, criando dashboards, alarmes e gráficos personalizados para monitorar o desempenho do seu sistema e identificar possíveis problemas.