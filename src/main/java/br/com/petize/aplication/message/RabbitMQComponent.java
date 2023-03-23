package br.com.petize.aplication.message;

import br.com.petize.aplication.dto.OrderCreatedEventDTO;
import br.com.petize.aplication.mapper.OrderMapper;
import br.com.petize.aplication.model.Order;
import br.com.petize.aplication.repository.OrderRepository;
import br.com.petize.aplication.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Componente responsável por enviar e receber mensagens via RabbitMQ e atualizar o status das ordens na base de dados.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQComponent {

    private final AmqpTemplate amqpTemplate;

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    @Value("${petize.rabbitmq.exchange}")
    private String exchange;

    @Value("${petize.rabbitmq.routingkey}")
    private String routingkey;

    /**
     * Envia uma mensagem contendo as informações de uma ordem criada.
     *
     * @param event as informações da ordem criada a serem enviadas.
     */
    public void send(OrderCreatedEventDTO event) {
        try {
            amqpTemplate.convertAndSend(exchange, routingkey, event);
            log.info("Send msg = {}", event);
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
    public void consume(OrderCreatedEventDTO message) {
        log.info("Received message -> {}", message);
        try {
            Order order = orderRepository.findById(message.getId()).get();

            if (!message.getValue().equals(order.getStatusOrder())) {
                order.setStatusOrder(message.getValue());
                orderRepository.save(order);
                log.info("Order update successful -> {}", Instant.now());

            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Não é possível atualizar dados!");
        }
    }

}