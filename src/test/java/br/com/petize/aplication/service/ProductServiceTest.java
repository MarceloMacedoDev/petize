package br.com.petize.aplication.service;

import br.com.petize.aplication.dto.ProductDto;
import br.com.petize.aplication.mapper.ProductMapper;
import br.com.petize.aplication.model.Product;
import br.com.petize.aplication.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository repository;

    private ProductDto productDto = new ProductDto();
    private Product product = new Product();

    @BeforeEach
    public void setUp() {
        productDto = new ProductDto();
        productDto.setId(1L);
        product = new Product();
        product.setId(1L); // Define um valor para o ID do objeto product simulado
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindAll() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(repository.findAll()).thenReturn(products);

        productService.findAll();

        verify(productMapper, times(1)).toDto(products);
    }

    @Test
    void testSave() {

        when(repository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);
        when(productMapper.toEntity(productDto)).thenReturn(product);

        ProductDto savedproductDto = productService.save(productDto);


        assertNotNull(savedproductDto.getId()); // Verifica se o ID foi atribuído corretamente
    }


    @Test
    void testDeleteById() {
        long id = 1L;

        productService.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testFindById() {
        long id = 1L;
        Product product = new Product();
        when(repository.findById(id)).thenReturn(java.util.Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(new ProductDto());

        productService.findById(id);

        verify(repository, times(1)).findById(id);
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void testUpdate() {
        long id = 1L;

        when(repository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(repository.save(any(Product.class))).thenReturn(product);

        ProductDto result = productService.update(productDto, id);

        assertNotNull(result);
    }

}