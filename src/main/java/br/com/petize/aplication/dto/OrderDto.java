package br.com.petize.aplication.dto;


import br.com.petize.aplication.model.ItensProducts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private long serialVersionUID;
    private Long id;
    private String name;
    private Instant createdAt;
    private List<ItensProducts> products = new ArrayList<>();
    private int qtd;
    private String statusOrder;

}