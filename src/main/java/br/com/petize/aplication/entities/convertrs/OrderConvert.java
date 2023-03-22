package br.com.petize.aplication.entities.convertrs;

import br.com.petize.aplication.entities.enums.StatusOrder;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderConvert  implements AttributeConverter<String, Integer>{
    @Override
    public Integer convertToDatabaseColumn(String s) {
        return StatusOrder.returnId(s);
    }

    @Override
    public String convertToEntityAttribute(Integer integer) {
        return StatusOrder.returnName(integer);
    }
}
