package br.com.petize.aplication.model.convertrs;

import br.com.petize.aplication.model.enums.StatusOrder;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Esta classe é um conversor que mapeia um valor String para um valor Integer para armazenamento em um banco de dados * <p>
 * e mapeia um valor Integer para um valor String para uso na aplicação.
 *
 * @author [Marcelo Macedo]
 */
@Converter
public class OrderConvert implements AttributeConverter<String, Integer> {

    /**
     * Converte um valor String para um valor Integer para armazenamento no banco de dados.
     *
     * @param s o valor String a ser convertido
     * @return o valor Integer correspondente
     */
    @Override
    public Integer convertToDatabaseColumn(String s) {
        return StatusOrder.returnId(s);
    }

    /**
     * Converte um valor Integer para um valor String para uso na aplicação.
     *
     * @param integer o valor Integer a ser convertido
     * @return o valor String correspondente
     */
    @Override
    public String convertToEntityAttribute(Integer integer) {
        return StatusOrder.returnName(integer);
    }
}