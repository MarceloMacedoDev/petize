package br.com.petize.aplication.service;

import br.com.petize.aplication.dto.OrderCreatedEventDTO;
import br.com.petize.aplication.dto.OrderDto;
import br.com.petize.aplication.mapper.OrderMapper;
import br.com.petize.aplication.message.RabbitMQComponent;
import br.com.petize.aplication.model.Order;
import br.com.petize.aplication.repository.OrderRepository;
import br.com.petize.aplication.service.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

/**
 * Serviço responsável por gerenciar as operações relacionadas a pedidos. Realiza operações CRUD
 * <p>
 * com o banco de dados por meio do OrderRepository e utiliza o OrderMapper para converter entre
 * <p>
 * objetos DTO e entidades.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper orderMapper;

    private final RabbitMQComponent mqComponent;

    private final ObjectMapper objectMapper;

    /**
     * Retorna todos os pedidos presentes no banco de dados.
     *
     * @return uma lista contendo os pedidos em formato DTO.
     */
    public List<OrderDto> findAll() {
        return orderMapper.toDto(repository.findAll());
    }

    /**
     * Salva um novo pedido no banco de dados.
     *
     * @param orderDto o pedido a ser salvo em formato DTO.
     * @return o pedido salvo em formato DTO.
     */
    public OrderDto save(OrderDto orderDto) {
        orderDto.setCreatedAt(Instant.now());
        Order entity = orderMapper.toEntity(orderDto);
        entity = repository.save(entity);

        return findById(entity.getId());
    }

    /**
     * Deleta um pedido do banco de dados por meio do seu ID.
     *
     * @param id o ID do pedido a ser deletado.
     */
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    /**
     * Retorna um pedido do banco de dados por meio do seu ID.
     *
     * @param id o ID do pedido a ser retornado.
     * @return o pedido encontrado em formato DTO.
     * @throws ResourceNotFoundException caso o pedido não seja encontrado.
     */
    public OrderDto findById(long id) {
        return orderMapper.
                toDto(repository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Não é possível excluir dados inexistentes!")
                ));
    }

    /**
     * Atualiza as informações de um pedido no banco de dados.
     *
     * @param orderDto as informações atualizadas do pedido em formato DTO.
     * @param id       o ID do pedido a ser atualizado.
     * @return o pedido atualizado em formato DTO.
     */
    public OrderDto update(OrderDto orderDto, Long id) {
        OrderDto data = findById(id);
        Order entity = orderMapper.toEntity(orderDto);
        BeanUtils.copyProperties(data, entity);
        return save(orderMapper.toDto(entity));
    }

    /**
     * Atualiza o status de um pedido por meio do envio de um evento para o componente RabbitMQ.
     *
     * @param event o evento contendo as informações necessárias para atualizar o status do pedido.
     */
    public void updateStatusOrder(OrderCreatedEventDTO event) {
        repository.findById(event.getId()).orElseThrow(
                        () -> {
                            log.error("Ordem inexistentes! = {}", event);
                           return   new ResourceNotFoundException("Ordem inexistentes!");

                        });
        mqComponent.send(event);
    }

}