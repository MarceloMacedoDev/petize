package br.com.petize.aplication.model;

import br.com.petize.aplication.model.convertrs.OrderConvert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_order")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	public static enum Conservacao {
		BOM, REGULAR, RUIM
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column()
	private Instant createdAt;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tb_itens_product_order",
			joinColumns = @JoinColumn(name = "order_id"),
			inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<Product> products = new ArrayList<>();

	private  int qtd;

	@Convert(converter = OrderConvert.class)
	private String statusOrder;
}
