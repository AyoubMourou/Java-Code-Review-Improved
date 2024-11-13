package schwarz.jobs.interview.coupon.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@Service
@RequiredArgsConstructor
@Slf4j // logging framework to control logging levels, better than sysout
public class CouponService {

    private final CouponRepository couponRepository;

    /**
     * Retrieves a coupon.
     * 
     * @param code The coupon code.
     * @return An Optional containing the Coupon if found, otherwise an exception is thrown.
     * @throws IllegalArgumentException If no coupon is found
     */
    public Optional<Coupon> getCoupon(final String code) {
    	// If a coupon is not found, it should be handled by .orElseThrow() or ifPresent();
        // also you con avoid the NoSuchElementException
        return couponRepository.findByCode(code)
        		.orElseThrow(() -> new IllegalArgumentException("Coupon with code " + code + " not found"));
    }

    /**
     * Applies a coupon discount to a given basket.
     * 
     * @param basket The basket to which the coupon will be applied.
     * @param code The coupon code to be applied to the basket.
     * @return The updated basket with the applied discount.
     * @throws RuntimeException If the basket value is negative and a discount is attempted to be applied.
     */
    public Optional<Basket> apply(final Basket basket, final String code) {

        return getCoupon(code).map(coupon -> {
        	// it's better to use BigDecimals built-in methods for comparisons
            if (basket.compareTo(BigDecimal.ZERO) >= 0) {
                if (basket.compareTo(BigDecimal.ZERO) > 0) {
                    basket.applyDiscount(coupon.getDiscount());
                } else // if (basket.getValue().doubleValue() == 0) { we don't need this validation
                    return basket;
                }
            } else {
                log.error("Tried to apply a negative discount to basket with code: {}", code);
                throw new RuntimeException("Can't apply negative discounts");
            }

            return basket;
        });
    }

	/**
	 * Creates a new coupon.
	 * 
	 * @param couponDTO The DTO containing the coupon details.
	 * @return The newly created Coupon object
	 * @throws IllegalArgumentException If the coupon DTO is is null or empty.
	 */
    // You can validate inputs early and throw a more meaningful exception,
    // catching a NullPointerException it might hide potential issues that needs be handled explicitly
    public Coupon createCoupon(final CouponDTO couponDTO) {
    	if (couponDTO == null || couponDTO.getCode() == null || couponDTO.getCode().isEmpty()) {
            throw new IllegalArgumentException("Coupon code cannot be null or empty");
        }

        Coupon coupon = Coupon.builder()
            .code(couponDTO.getCode().toLowerCase())
            .discount(couponDTO.getDiscount())
            .minBasketValue(couponDTO.getMinBasketValue())
            .build();

        return couponRepository.save(coupon);
    }

    /**
     * Retrieves a list of coupons.
     * 
     * @param couponRequestDTO The DTO containing a list of coupon codes.
     * @return A list of Coupon objects.
     * @throws IllegalArgumentException If any of the coupon codes are invalid or not found.
     */
    public List<Coupon> getCoupons(final CouponRequestDTO couponRequestDTO) {

        final ArrayList<Coupon> foundCoupons = new ArrayList<>();

        // couponRequestDTO.getCodes().forEach(code -> foundCoupons.add(couponRepository.findByCode(code).get()));
        // If a coupon is not found, it should be handled by .orElseThrow() or ifPresent();
        // also you con avoid the NoSuchElementException
        for (String code : couponRequestDTO.getCodes()) {
            Coupon coupon = couponRepository.findByCode(code)
            // if we want, we can create a class to handle exceptions if we have a lot of them, e.g. CuponNotFoundException
                .orElseThrow(() -> new IllegalArgumentException("Coupon with code " + code + " not found"));
            foundCoupons.add(coupon);
        }
        
        return foundCoupons;
    }
}
