package br.com.petize.aplication.service;

import br.com.petize.aplication.dto.OrderDto;
import br.com.petize.aplication.model.Order;
import br.com.petize.aplication.mapper.OrderMapper;
import br.com.petize.aplication.repository.OrderRepository;
import br.com.petize.aplication.service.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository repository, OrderMapper orderMapper) {
        this.repository = repository;
        this.orderMapper = orderMapper;
    }

    public OrderDto save(OrderDto orderDto) {
        Order entity = orderMapper.toEntity(orderDto);
        return orderMapper.toDto(repository.save(entity));
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