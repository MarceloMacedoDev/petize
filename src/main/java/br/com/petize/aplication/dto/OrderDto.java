package br.com.petize.aplication.dto;


import br.com.petize.aplication.model.ItensProducts;
import br.com.petize.aplication.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Instant createdAt;
    private List<Product> products = new ArrayList<>();
    private int qtd;
    @JsonProperty("status")
    private String statusOrder;

}