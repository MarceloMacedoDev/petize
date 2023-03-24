package br.com.petize.aplication.controller;

import br.com.petize.aplication.dto.ProductDto;
import br.com.petize.aplication.service.ProductService;
import br.com.petize.aplication.service.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador responsável por gerenciar as operações de produtos.
 */
@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Construtor que recebe o serviço de produtos.
     *
     * @param productService serviço de produtos
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Salva um novo produto.
     *
     * @param productDto informações do produto a ser salvo
     * @return resposta com status HTTP indicando o sucesso da operação
     */
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Validated ProductDto productDto) {
        productService.save(productDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca um produto pelo seu ID.
     *
     * @param id identificador do produto
     * @return resposta com o produto encontrado e status HTTP indicando o sucesso da operação
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable("id") Long id) {
        ProductDto product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Exclui um produto pelo seu ID.
     *
     * @param id identificador do produto a ser excluído
     * @return resposta com status HTTP indicando o sucesso da operação
     * @throws ResourceNotFoundException se o produto não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Optional.ofNullable(productService.findById(id)).orElseThrow(() -> {
            log.error("Não é possível excluir produtos inexistentes!");
            return new ResourceNotFoundException("Não é possível excluir produtos inexistentes!");
        });
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Lista todos os produtos cadastrados.
     *
     * @return resposta com a lista de produtos e status HTTP indicando o sucesso da operação
     */
    @GetMapping({"/", ""})
    public ResponseEntity<List<ProductDto>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    /**
     * Atualiza as informações de um produto.
     *
     * @param productDto informações atualizadas do produto
     * @param id         identificador do produto a ser atualizado
     * @return resposta com status HTTP indicando o sucesso da operação
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Validated ProductDto productDto, @PathVariable("id") Long id) {
        productService.update(productDto, id);
        return ResponseEntity.ok().build();
    }
}