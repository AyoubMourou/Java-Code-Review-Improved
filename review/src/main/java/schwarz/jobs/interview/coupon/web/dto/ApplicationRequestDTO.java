package schwarz.jobs.interview.coupon.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import schwarz.jobs.interview.coupon.core.services.model.Basket;

@Data
@Builder
public class ApplicationRequestDTO {
	// it's a good practice to use JavaDocs
	
	/**
	 * The code associated with the coupon.
	 * This must be a non-blank value.
	 */
    @NotBlank
    private String code;

    // Ensures that the fields within the Basket object are validated as well, 
    // if there are any validation annotations on its fields (e.g., @NotNull, @Positive).
    /**
	 * The user's basket.
	 * This must be a non-null value and a valid one.
	 */
    @NotNull
    @Valid
    private Basket basket;

    // Review: as I said in the Coupon class, we can use the Lombok Annotations as this class to reduce code
    // and avoid boilerplate or use the manual way to control the implementation.
}
