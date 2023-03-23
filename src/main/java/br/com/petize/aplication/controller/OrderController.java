package br.com.petize.aplication.controller;

import br.com.petize.aplication.dto.OrderCreatedEventDTO;
import br.com.petize.aplication.dto.OrderDto;
import br.com.petize.aplication.service.OrderService;
import br.com.petize.aplication.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador para manipulação de pedidos.
 *
 * <p>Esta classe é responsável por receber e processar as requisições referentes a pedidos, utilizando o
 * serviço de pedidos {@link OrderService} para realizar as operações necessárias.
 *
 * <p>As operações suportadas incluem salvar, buscar, atualizar e excluir pedidos, além de atualizar o status
 * de um pedido utilizando o RabbitMQ.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    /**
     * Serviço responsável por realizar as operações de pedidos.
     */
    private final OrderService orderService;

    /**
     * Salva um novo pedido na base de dados.
     *
     * @param orderDto DTO contendo as informações do pedido a ser salvo.
     * @return status HTTP 201 (Created).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> save(@RequestBody @Validated OrderDto orderDto) {
        orderService.save(orderDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Método que retorna todos os objetos OrderDto através da requisição HTTP GET na raiz do endpoint do controlador ou no endpoint '/'.
     *
     * @return ResponseEntity<List < OrderDto>> com a lista de todos os objetos OrderDto encontrados.
     */
    @GetMapping({"", "/"})
    public ResponseEntity<List<OrderDto>> findAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    /**
     * Método que retorna um objeto OrderDto correspondente ao id fornecido através da requisição HTTP GET para o endpoint '/{id}'.
     *
     * @param id um objeto Long que representa o id do objeto OrderDto que se deseja encontrar.
     * @return ResponseEntity<OrderDto> contendo o objeto OrderDto correspondente ao id fornecido.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable("id") Long id) {
        OrderDto order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Método que exclui o objeto Order correspondente ao id fornecido através da requisição HTTP DELETE para o endpoint '/{id}'.
     *
     * @param id um objeto Long que representa o id do objeto OrderDto que se deseja excluir.
     * @return ResponseEntity<Void> com um código de status HTTP OK.
     * @throws ResourceNotFoundException se não for possível encontrar um objeto Order correspondente ao id fornecido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Optional.ofNullable(orderService.findById(id)).orElseThrow(() -> {
            log.error("Não é possível excluir dados inexistentes!");
            return new ResourceNotFoundException("Não é possível excluir dados inexistentes!");
        });
        orderService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Atualiza uma ordem existente na base de dados com as informações contidas em {@code orderDto}.
     *
     * @param orderDto as informações para atualização da ordem.
     * @param id       o ID da ordem a ser atualizada.
     * @return um objeto ResponseEntity<Void> com o status HTTP 200 OK em caso de sucesso.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Validated OrderDto orderDto, @PathVariable("id") Long id) {
        orderService.update(orderDto, id);
        return ResponseEntity.ok().build();
    }

    /**
     * Atualiza o status de uma ordem existente na base de dados com as informações contidas em {@code eventDTO}.
     *
     * @param eventDTO as informações para atualização do status da ordem.
     * @return um objeto ResponseEntity<Void> com o status HTTP 200 OK em caso de sucesso.
     */
    @PostMapping("/alterstatus")
    public ResponseEntity<Void> updateRabbitMQ(@RequestBody @Validated OrderCreatedEventDTO eventDTO) {
        orderService.updateStatusOrder(eventDTO);
        return ResponseEntity.ok().build();
    }
}