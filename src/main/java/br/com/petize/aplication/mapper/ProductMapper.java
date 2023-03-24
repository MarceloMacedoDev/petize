package br.com.petize.aplication.mapper;

import org.mapstruct.Mapper;

import br.com.petize.aplication.dto.ProductDto;
import br.com.petize.aplication.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDto, Product> {
}