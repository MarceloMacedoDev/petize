package br.com.petize.aplication.model;

import br.com.petize.aplication.model.convertrs.OrderConvert;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_order")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant createdAt;

	@OneToMany
	private List<ItensProducts> products = new ArrayList<>();

	private  int qtd;

	@Convert(converter = OrderConvert.class)
	private String statusOrder;
}
