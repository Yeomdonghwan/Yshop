package yeom.yshop.entity;

import jakarta.persistence.*;

@Entity
public class Address extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    private String city;
    private String street;
    private String zipcode;
}
