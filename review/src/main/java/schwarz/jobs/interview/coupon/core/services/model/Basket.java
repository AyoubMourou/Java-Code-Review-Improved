package schwarz.jobs.interview.coupon.core.services.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Basket {

    @NotNull
    private BigDecimal value;

    // to avoid null discounts we need to initialize the field to zero
    private BigDecimal appliedDiscount = BigDecimal.ZERO;

    private boolean applicationSuccessful;

    // it's a good practice to use JavaDocs and clarify the purpose of the method
    /**
     * Apply a discount to the basket.
     * 
     * @param discount the discount to be applied, must be non-negative.
     * @throws IllegalArgumentException if the discount is null or negative.
     */
    public void applyDiscount(final BigDecimal discount) {
    	// we need a validation check to ensure that the discount isn't null or a negative discount 
    	if (discount == null || discount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount cannot be null or negative.");
        }

        this.appliedDiscount = discount;
        this.applicationSuccessful = true; // Mark as successful when discount is applied
    }
    
    // Review: as I said in the Coupon class, we can use the Lombok Annotations as this class to reduce code
    // and avoid boilerplate or use the manual way to control the implementation.

}
