package br.com.petize.aplication.service;

import br.com.petize.aplication.dto.OrderCreatedEventDTO;
import br.com.petize.aplication.dto.OrderDto;
import br.com.petize.aplication.events.RabbitMQComponent;
import br.com.petize.aplication.mapper.OrderMapper;
import br.com.petize.aplication.model.Order;
import br.com.petize.aplication.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private RabbitMQComponent mqComponent;

    @Mock
    private OrderRepository repository;
    private OrderDto orderDto = new OrderDto();
    private Order order = new Order();
    @Mock
    private OrderMapper orderMapper;

    @BeforeEach
    public void setUp() {
        orderDto = new OrderDto();
        orderDto.setCreatedAt(Instant.now());
        orderDto.setId(1L);
        order = new Order();
        order.setId(1L); // Define um valor para o ID do objeto Order simulado
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindAll() {
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(repository.findAll()).thenReturn(orders);

        orderService.findAll();

        verify(orderMapper, times(1)).toDto(orders);
    }

    @Test
    void testSave() {
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);
        when(orderMapper.toEntity(orderDto)).thenReturn(order);
        when(repository.save(order)).thenReturn(order);

        OrderDto savedOrderDto = orderService.save(orderDto);


        assertNotNull(savedOrderDto.getId()); // Verifica se o ID foi atribuído corretamente
    }


    @Test
    void testDeleteById() {
        long id = 1L;

        orderService.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testFindById() {
        long id = 1L;
        Order order = new Order();
        when(repository.findById(id)).thenReturn(java.util.Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(new OrderDto());

        orderService.findById(id);

        verify(repository, times(1)).findById(id);
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    void testUpdate() {
        long id = 1L;

        when(repository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        when(orderMapper.toEntity(orderDto)).thenReturn(order);
        when(repository.save(any(Order.class))).thenReturn(order);

        OrderDto result = orderService.update(orderDto, id);

        verify(orderMapper, times(1)).toEntity(orderDto);
        assertNotNull(result);
    }

    @Test
    void testUpdateStatusOrder() {
        OrderCreatedEventDTO event = new OrderCreatedEventDTO();
        event.setId(1L);
        when(repository.findById(event.getId())).thenReturn(Optional.of(order));

        orderService.updateStatusOrder(event);

        verify(repository, times(1)).findById(event.getId());
    }
}
