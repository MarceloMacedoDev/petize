package br.com.petize.aplication.message;

import br.com.petize.aplication.mapper.OrderMapper;
import br.com.petize.aplication.model.Order;
import br.com.petize.aplication.repository.OrderRepository;
import br.com.petize.aplication.service.OrderService;
import br.com.petize.aplication.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import br.com.petize.aplication.dto.OrderCreatedEventDTO;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

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

    public void send(OrderCreatedEventDTO event) {
        amqpTemplate.convertAndSend(exchange, routingkey, event);
        log.info("Send msg = {}", event);

    }

    @RabbitListener(queues = {"${petize.rabbitmq.queue}"})
    public void consume(OrderCreatedEventDTO message) {
        log.info( "Received message -> {}", message);
       try {
           Order order = orderRepository.findById(message.getId()).get();
           if (!message.getValue().equals(order.getStatusOrder())) {
               order.setStatusOrder(message.getValue());
               orderRepository.save(order);
               log.info("Order update successful -> {}", Instant.now());
           }
       }catch ( Exception e){
           throw new ResourceNotFoundException("Não é possível lê RabbitMQ!");
       }
    }

}