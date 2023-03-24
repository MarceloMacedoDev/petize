package br.com.petize.aplication.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.petize.aplication.dto.ProductDto;
import br.com.petize.aplication.mapper.ProductMapper;
import br.com.petize.aplication.model.Product;
import br.com.petize.aplication.repository.ProductRepository;
import br.com.petize.aplication.service.exception.ResourceNotFoundException;
import br.com.petize.aplication.util.Util;

/**
 * Classe responsável por realizar as operações de negócio relacionadas a produtos.
 */

@Service
@Transactional
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper productMapper;

    /**
     * Construtor que recebe uma instância de repositório e mapeador de produtos.
     *
     * @param repository    instância do repositório de produtos
     * @param productMapper instância do mapeador de produtos
     */
    public ProductService(ProductRepository repository, ProductMapper productMapper) {
        this.repository = repository;
        this.productMapper = productMapper;
    }

    /**
     * Salva um novo produto no banco de dados.
     *
     * @param productDto DTO do produto a ser salvo
     * @return DTO do produto salvo
     */
    public ProductDto save(ProductDto productDto) {
        Product entity = productMapper.toEntity(productDto);
        return productMapper.toDto(repository.save(entity));
    }

    /**
     * Deleta um produto pelo seu identificador.
     *
     * @param id identificador do produto a ser deletado
     */
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    /**
     * Busca um produto pelo seu identificador.
     *
     * @param id identificador do produto a ser buscado
     * @return DTO do produto encontrado
     * @throws ResourceNotFoundException caso o produto não seja encontrado
     */
    public ProductDto findById(long id) throws ResourceNotFoundException {
        return productMapper.toDto(repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Não foi possível encontrar o produto com o id " + id)));
    }

    /**
     * Busca produtos baseados em uma condição e retorna uma página.
     *
     * @param productDto DTO com as condições para busca dos produtos
     * @param pageable   informações da paginação
     * @return página com a lista de produtos encontrados
     */
    public Page<ProductDto> findByCondition(ProductDto productDto, Pageable pageable) {
        Page<Product> entityPage = repository.findAll(pageable);
        List<Product> entities = entityPage.getContent();
        return new PageImpl<>(productMapper.toDto(entities), pageable, entityPage.getTotalElements());
    }

    /**
     * Atualiza as informações de um produto pelo seu identificador.
     *
     * @param productDto DTO com as informações atualizadas do produto
     * @param id         identificador do produto a ser atualizado
     * @return DTO do produto atualizado
     */
    public ProductDto update(ProductDto productDto, Long id) {
        ProductDto data = findById(id);
        Product entity = productMapper.toEntity(productDto);
        BeanUtils.copyProperties(data, entity, Util.getNullProperties(data));
        return save(productMapper.toDto(entity));
    }

    /**
     * Busca todos os produtos cadastrados no banco de dados.
     *
     * @return lista com todos os produtos encontrados
     */
    public List<ProductDto> findAll() {
        return productMapper.toDto(repository.findAll());
    }
}