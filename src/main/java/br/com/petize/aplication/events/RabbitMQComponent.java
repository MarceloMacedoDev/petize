package br.com.petize.aplication.events;

import java.time.Instant;

import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.petize.aplication.dto.OrderCreatedEventDTO;
import br.com.petize.aplication.model.Order;
import br.com.petize.aplication.repository.OrderRepository;
import br.com.petize.aplication.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Componente responsável por enviar e receber mensagens via RabbitMQ e atualizar o status das ordens na base de dados.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQComponent {

    private final AmqpTemplate amqpTemplate;

    private final OrderRepository orderRepository;

    private final RetryComponent retryComponent;


    @Value("${petize.rabbitmq.exchange}")
    private String exchange;

    @Value("${petize.rabbitmq.routingkey}")
    private String routingkey;

    private final MeterRegistry meterRegistry;


    /**
     * Envia uma mensagem contendo as informações de uma ordem criada.
     *
     * @param event as informações da ordem criada a serem enviadas.
     */
    public void eventPublisher(OrderCreatedEventDTO event) {
        try {
            final Counter sentMessagesCounter = Counter.builder("rabbitmq_messages_sent_total")
                    .description("Total number of messages sent to RabbitMQ")
                    .register(meterRegistry);
            final Timer sentMessagesTimer = meterRegistry.timer("rabbitmq_event_send_duration_time");
            retryComponent.executeWithRetry(() -> {
                sentMessagesCounter.increment();

                Timer.Sample sample = Timer.start(meterRegistry);
                amqpTemplate.convertAndSend(exchange, routingkey, event);
                log.info("Send msg -> {}", event);
                sample.stop(sentMessagesTimer);
                return null;
            });


        } catch (Exception e) {
            throw new ResourceNotFoundException("Erro de processamento de evento!");
        }
    }

    /**
     * Recebe uma mensagem contendo as informações de uma ordem criada e atualiza o status da ordem correspondente na base de dados.
     *
     * @param message as informações da ordem criada recebidas.
     * @throws ResourceNotFoundException caso ocorra algum erro ao atualizar os dados da ordem na base de dados.
     */
    @RabbitListener(queues = {"${petize.rabbitmq.queue}"})
    public void orderCreatedEventListener(OrderCreatedEventDTO message) {
        log.info("Received message -> {}", message);
        final Counter receivedMessagesCounter = Counter.builder("rabbitmq_messages_received_total")
                .description("Total number of messages received from RabbitMQ")
                .register(meterRegistry);
        final Timer receivedMessagesTimer = Timer.builder("rabbitmq_event_received_duration_time")
                .description("Duration of receiving messages from RabbitMQ")
                .register(meterRegistry);
        try {
            Order order = orderRepository.findById(message.getId()).get();
            receivedMessagesCounter.increment();

            Timer.Sample sample = Timer.start(meterRegistry);
            if (!message.getValue().equals(order.getStatusOrder())) {
                order.setStatusOrder(message.getValue());
                orderRepository.save(order);
                log.info("Order update successful -> {}", Instant.now());

            }
            sample.stop(receivedMessagesTimer);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Não é possível atualizar dados!");
        }
    }

}