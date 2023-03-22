package br.com.petize.aplication.mapper;

import br.com.petize.aplication.dto.OrderDto;
import br.com.petize.aplication.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDto, Order> {
}