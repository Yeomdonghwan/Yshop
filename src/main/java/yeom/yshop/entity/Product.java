package yeom.yshop.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Product extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;
    private double price;

    private Long stock_quantity;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

}
