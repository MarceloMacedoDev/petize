package br.com.petize.aplication.message;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração responsável por definir a configuração da conexão e as configurações da fila, troca e rota do RabbitMQ.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${petize.rabbitmq.queue}")
    String queueName;

    @Value("${petize.rabbitmq.exchange}")
    String exchange;

    @Value("${petize.rabbitmq.routingkey}")
    private String routingkey;

    /**
     * Cria uma fila durável, exclusiva e auto-excluível no RabbitMQ com o nome definido em {@code queueName}.
     *
     * @return a fila criada.
     */
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(queueName)
                .autoDelete()
                .exclusive()
                .build();
    }

    /**
     * Cria uma troca direta no RabbitMQ com o nome definido em {@code exchange}.
     *
     * @return a troca criada.
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    /**
     * Cria uma ligação entre a fila criada em {@code queue} e a troca criada em {@code exchange} utilizando a rota definida em {@code routingkey}.
     *
     * @param queue    a fila a ser ligada à troca.
     * @param exchange a troca a ser ligada à fila.
     * @return a ligação criada.
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }

    /**
     * Define o conversor de mensagens para JSON a ser utilizado pelo RabbitMQ.
     *
     * @return o conversor de mensagens para JSON.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Retorna um objeto AmqpTemplate que encapsula um RabbitTemplate configurado com o
     * ConnectionFactory fornecido e um jsonMessageConverter.
     *
     * @param connectionFactory a ConnectionFactory que será usada para criar o RabbitTemplate
     * @return um objeto AmqpTemplate configurado com o RabbitTemplate e o MessageConverter
     */
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
