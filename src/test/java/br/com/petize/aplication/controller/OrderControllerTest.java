package br.com.petize.aplication.controller;

import br.com.petize.aplication.dto.OrderCreatedEventDTO;
import br.com.petize.aplication.dto.OrderDto;
import br.com.petize.aplication.events.RabbitMQComponent;
import br.com.petize.aplication.mapper.OrderMapper;
import br.com.petize.aplication.model.Order;
import br.com.petize.aplication.repository.OrderRepository;
import br.com.petize.aplication.service.OrderService;
import br.com.petize.aplication.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void save_shouldCallOrderServiceSaveMethod() {
        // Arrange
        OrderDto orderDto = new OrderDto();

        // Act
        ResponseEntity<Void> response = orderController.save(orderDto);

        // Assert
        verify(orderService).save(orderDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findAll_shouldCallOrderServiceFindAllMethod() {
        // Arrange
        List<OrderDto> orders = new ArrayList<>();

        // Mock
        when(orderService.findAll()).thenReturn(orders);

        // Act
        ResponseEntity<List<OrderDto>> response = orderController.findAll();

        // Assert
        verify(orderService).findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    @Test
    void findById_shouldCallOrderServiceFindByIdMethod() {
        // Arrange
        Long id = 1L;
        OrderDto orderDto = new OrderDto();

        // Mock
        when(orderService.findById(id)).thenReturn(orderDto);

        // Act
        ResponseEntity<OrderDto> response = orderController.findById(id);

        // Assert
        verify(orderService).findById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDto, response.getBody());
    }



    @Test
    void update_shouldCallOrderServiceUpdateMethod() {
        // Arrange
        Long id = 1L;
        OrderDto orderDto = new OrderDto();

        // Act
        ResponseEntity<Void> response = orderController.update(orderDto, id);

        // Assert
        verify(orderService).update(orderDto, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateRabbitMQ_shouldCallOrderServiceUpdateStatusOrderMethod() {
        // Arrange
        OrderCreatedEventDTO eventDTO = new OrderCreatedEventDTO();

        // Act
        ResponseEntity<Void> response = orderController.updateRabbitMQ(eventDTO);

        // Assert
        verify(orderService).updateStatusOrder(eventDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
   /* @Test
    void givenInvalidId_whenDeleteOrder_thenThrowsResourceNotFoundException() {
        // Arrange
        Long invalidId = 999L;
        when(orderService.findById(invalidId)).thenReturn(null);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            orderController.delete(invalidId);
        });
         verify(orderService, never()).deleteById(anyLong());
    }
*/
/*    @Test
    void givenValidId_whenDeleteOrder_thenOrderServiceDeleteByIdMethodShouldBeCalled() {
        // Arrange
        Long validId = 123L;
        Order order = new Order();
        when(orderService.findById(validId)).thenReturn(new OrderDto());

        // Act
        ResponseEntity<Void> response = orderController.delete(validId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderService, times(1)).deleteById(validId);
    }*/
}