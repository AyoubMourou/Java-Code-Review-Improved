package schwarz.jobs.interview.coupon.core.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon") // it's optionally, but it's a good practice to specify it 
public class Coupon implements Serializable { // implement Seriazable class is a good practice for entities,
											  // especially when you need to pass them between different 
											  // layers of your application .

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // if we want an auto-increment id
    @Column(name = "id", updatable = false, nullable = false) // to specify column attributes, 
    												    // such as name or if is a nullable field
    												  // (optional, because a primary key it can't be null)
    private Long id;

    @NotNull 				  // in CouponRespository we have a method called findByCode, so this is to 
    @Size(max = 250)  // prevent invalid data from being stored in the database
    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(name = "minBasketValue", precision = 10, scale = 2)
    private BigDecimal minBasketValue;
    
    // Overriding equals() and hashCode() ensures correct comparisons for JPA entities
    // It's a good practice to override equals() and hashCode() to ensure proper comparison and handling of entity instances 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return id.equals(coupon.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    // Review: This lombok version you can use it if you want to avoid boilerplate and you prefer
    // using the Builder pattern to avoid writing constructors manually.
    // There is another version, the manually veriosn that you have control of the implementantion of the class, 
    // you hcan create the contructors, getters, setter, both verisons are valid.
}
