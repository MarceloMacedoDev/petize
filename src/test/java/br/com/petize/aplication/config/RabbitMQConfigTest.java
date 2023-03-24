package br.com.petize.aplication.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(RabbitMQConfig.class)
@ExtendWith(MockitoExtension.class)
class RabbitMQConfigTest {

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private AmqpTemplate amqpTemplate;

    @Test
    void  testQueue() {
        Queue queue = rabbitMQConfig.queue();
        // perform any necessary assertions on the queue object
    }

    @Test
    void  testExchange() {
        DirectExchange exchange = rabbitMQConfig.exchange();
        // perform any necessary assertions on the exchange object
    }

    @Test
    void  testBinding() {
        Queue queue = new Queue("testQueue", true);
        DirectExchange exchange = new DirectExchange("testExchange");
        Binding binding = rabbitMQConfig.binding(queue, exchange);
        // perform any necessary assertions on the binding object
    }

    @Test
    void  testJsonMessageConverter() {
        MessageConverter converter = rabbitMQConfig.jsonMessageConverter();
        // perform any necessary assertions on the converter object
    }

    @Test
    void testRabbitTemplate() {
        AmqpTemplate amqpTemplate = rabbitMQConfig.rabbitTemplate(connectionFactory);
        assertNotNull(amqpTemplate);
    }
}
