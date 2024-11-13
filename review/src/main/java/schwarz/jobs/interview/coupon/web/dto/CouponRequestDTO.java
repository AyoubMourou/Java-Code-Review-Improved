package schwarz.jobs.interview.coupon.web.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponRequestDTO {
	// it's a good practice to use JavaDocs
	
	/**
     * The list of coupon codes to be applied.
     * This list must not be empty, and each code must not be blank.
     */
    @NotNull
    @Size(min = 1, message = "The list of codes must contain at least one code.")
    // to ensure that the code isn't empty if it's necessary
    private List<@NotBlank String> codes; // ensures that no code in the list is null, empty or just whitespaces
    
    // Review: as I said in the Coupon class, we can use the Lombok Annotations as this class to reduce code
    // and avoid boilerplate or use the manual way to control the implementation.
}
