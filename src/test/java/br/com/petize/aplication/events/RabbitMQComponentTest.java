package br.com.petize.aplication.events;

import br.com.petize.aplication.dto.OrderCreatedEventDTO;
import br.com.petize.aplication.model.Order;
import br.com.petize.aplication.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQComponentTest {

    @Mock
    private AmqpTemplate amqpTemplate;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RetryComponent retryComponent;
    @InjectMocks
    private RabbitMQComponent rabbitMQComponent;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testEventPublisher() {
        // Mock objects
        OrderCreatedEventDTO event = new OrderCreatedEventDTO();
        String exchange = "test.exchange";
        String routingkey = "test.routingkey";
        doNothing().when(amqpTemplate).convertAndSend(null, null, event);
        rabbitMQComponent.eventPublisher(event);
        // Verify calls
        verify(amqpTemplate, times(1)).convertAndSend(null, null, event);
        verifyNoMoreInteractions(amqpTemplate);
    }


    @Test
    void testOrderCreatedEventListener() {
        // given
        Long orderId = 1L;
        String status = "COMPLETE";
        Order order = new Order();
        order.setId(orderId);
        order.setStatusOrder(status);
        OrderCreatedEventDTO event = new OrderCreatedEventDTO();
        event.setId(orderId);
        event.setValue(status);


        rabbitMQComponent = new RabbitMQComponent(amqpTemplate, orderRepository, retryComponent);

        lenient().when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));



        // when
        rabbitMQComponent.orderCreatedEventListener(event);

//        // then
        verify(orderRepository, times(1)).findById(orderId);
//        verify(orderRepository, times(1)).save(order);
        assertEquals(status, order.getStatusOrder());
    }
}
