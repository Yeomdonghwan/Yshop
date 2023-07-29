package yeom.yshop.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity @Getter
public class Cart extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
