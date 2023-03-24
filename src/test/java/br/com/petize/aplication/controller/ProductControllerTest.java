package br.com.petize.aplication.controller;

import br.com.petize.aplication.dto.OrderDto;
import br.com.petize.aplication.dto.ProductDto;
import br.com.petize.aplication.model.Order;
import br.com.petize.aplication.service.ProductService;
import br.com.petize.aplication.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

     
    @Test
    void save_shouldCallproductServiceSaveMethod() {
        // Arrange
        ProductDto productDto = new ProductDto();

        // Act
        ResponseEntity<Void> response = productController.save(productDto);

        // Assert
        verify(productService).save(productDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findAll_shouldCallproductServiceFindAllMethod() {
        // Arrange
        List<ProductDto> orders = new ArrayList<>();

        // Mock
        when(productService.findAll()).thenReturn(orders);

        // Act
        ResponseEntity<List<ProductDto>> response = productController.findAll();

        // Assert
        verify(productService).findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    @Test
    void findById_shouldCallproductServiceFindByIdMethod() {
        // Arrange
        Long id = 1L;
        ProductDto productDto = new ProductDto();

        // Mock
        when(productService.findById(id)).thenReturn(productDto);

        // Act
        ResponseEntity<ProductDto> response = productController.findById(id);

        // Assert
        verify(productService).findById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }



    @Test
    void update_shouldCallproductServiceUpdateMethod() {
        // Arrange
        Long id = 1L;
        ProductDto productDto = new ProductDto();

        // Act
        ResponseEntity<Void> response = productController.update(productDto, id);

        // Assert
        verify(productService).update(productDto, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
/*    @Test
    void givenInvalidId_whenDeleteOrder_thenThrowsResourceNotFoundException() {
        // Arrange
        Long invalidId = 999L;
        when(productService.findById(invalidId)).thenReturn(null);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productController.delete(invalidId);
        });
        verify(productService, never()).deleteById(anyLong());
    }*/

/*    @Test
    void givenValidId_whenDeleteOrder_thenproductServiceDeleteByIdMethodShouldBeCalled() {
        // Arrange
        Long validId = 123L;
        Order order = new Order();
        when(productService.findById(validId)).thenReturn(new ProductDto());

        // Act
        ResponseEntity<Void> response = productController.delete(validId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(productService, times(1)).deleteById(validId);
    }*/
}
