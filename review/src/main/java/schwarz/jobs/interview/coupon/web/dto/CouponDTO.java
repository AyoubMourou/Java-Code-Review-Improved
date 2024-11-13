package schwarz.jobs.interview.coupon.web.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponDTO {
	// it's a good practice to use JavaDocs

	/**
	 * The discount percentage of the coupon.
	 * This should be a positive number.
	 */
	@NotNull // to ensure that the field is not null
	@Positive // to ensure that is a non-negative number
    private BigDecimal discount;

	/**
	 * The code associated with the coupon.
	 * This must be a non-blank value.
	 */
	@NotBlank // to ensure the field is not empty or blank
    private String code;

	/**
	 * This should be a positive number.
	 */
    @Positive // to ensure that is a non-negative number
    private BigDecimal minBasketValue;

	// Review: as I said in the Coupon class, we can use the Lombok Annotations as this class to reduce code
    // and avoid boilerplate or use the manual way to control the implementation.
}
