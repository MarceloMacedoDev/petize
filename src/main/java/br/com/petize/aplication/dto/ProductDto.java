package br.com.petize.aplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;

    private String description;
    private Double price;
 }