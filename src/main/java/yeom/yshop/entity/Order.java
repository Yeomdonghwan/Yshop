package yeom.yshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import yeom.yshop.enums.OrderStatus;

@Entity @Getter
@Table(name = "orders")
public class Order extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="account_id")
    private Account account;

    private double totalPrice;

    private OrderStatus status;
}
