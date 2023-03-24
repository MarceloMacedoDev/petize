package br.com.petize.aplication.mapper;

import br.com.petize.aplication.dto.ProductDto;
import br.com.petize.aplication.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDto, Product> {
}