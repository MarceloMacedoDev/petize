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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper orderMapper;

    private final RabbitMQComponent mqComponent;

    private final ObjectMapper objectMapper;

    public List<OrderDto> findAll() {
        return orderMapper.toDto(repository.findAll());
    }


    public OrderDto save(OrderDto orderDto) {
        orderDto.setCreatedAt(Instant.now());
        Order entity = orderMapper.toEntity(orderDto);
        entity=repository.save(entity);
        OrderCreatedEventDTO event =   OrderCreatedEventDTO.builder()
                .id(entity.getId())
                .value( entity.getStatusOrder ())
                .build();
        mqComponent.send(event);
        return findById(entity.getId());
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }

    public OrderDto findById(long id) {
        return orderMapper.
                toDto(repository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Não é possível excluir dados inexistentes!")
                ));
    }


    public OrderDto update(OrderDto orderDto, Long id) {
        OrderDto data = findById(id);
        Order entity = orderMapper.toEntity(orderDto);
        BeanUtils.copyProperties(data, entity);
        return save(orderMapper.toDto(entity));
    }

}