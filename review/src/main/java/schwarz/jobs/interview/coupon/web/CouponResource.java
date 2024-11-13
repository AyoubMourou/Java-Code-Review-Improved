package schwarz.jobs.interview.coupon.web;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

// @Controller
@RestController // this controller is primarily serving RESTful endpoints, so it's better to use
				// RestController, so we don't need to manually add @RequestBody in each method
@RequiredArgsConstructor
@RequestMapping("/api/v1") // we can use a version to further maintnainability
@Slf4j
@Api(tags = "Coupon Managment API") // adds a description for the controller in Swagger UI
public class CouponResource {

    private final CouponService couponService;

    /**
     * Applies a coupon to the basket and returns the updated basket.
     *
     * @param applicationRequestDTO containing basket and coupon code
     * @return ResponseEntity containing the updated basket
     */
    @ApiOperation(value = "Applies currently active promotions and coupons from the request to the requested Basket - Version 1")
    @PostMapping(value = "/apply")
    public ResponseEntity<Basket> apply(
        @ApiParam(value = "Provides the necessary basket and customer information required for the coupon application", required = true)
        @RequestBody @Valid final ApplicationRequestDTO applicationRequestDTO) {

        log.info("Applying coupon");

        final Optional<Basket> basket =
            couponService.apply(applicationRequestDTO.getBasket(), applicationRequestDTO.getCode());

        if (basket.isEmpty()) {
        	// we can add more details with logs to help us during debugging and tracking the requests
        	log.warn("Coupon application failed: Basket not found for coupon code: {}", applicationRequestDTO.getCode());
            return ResponseEntity.badRequest.build(); // it's better to return bad request, that indicates
        } 											  // that the request couldn't be processed

        if (!applicationRequestDTO.getBasket().isApplicationSuccessful()) {
            log.warn("Coupon application unsuccessful for code: {}", applicationRequestDTO.getCode());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        log.info("Applied coupon");

        return ResponseEntity.ok().body(applicationRequestDTO.getBasket());
    }

    /**
     * Creates a new coupon.
     *
     * @param couponDTO the coupon details
     * @return ResponseEntity indicating the result of the operation
     */
    // It's better to return a 201 Created response when a resource is created, with the new location
    // indicated in the URI
    @ApiOperation(value = "Create a new coupon")
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody @Valid final CouponDTO couponDTO) {

        final Coupon coupon = couponService.createCoupon(couponDTO);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(coupon.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Gets a list of coupons based on the provided parametro.
     *
     * @param The parametro
     * @return List of coupons
     */
    // Get requests shouldn't have a body, the typical approach for providing parameters 
    // is to use query paramters, e.g. /coupons?coupon=1093
    @ApiOperation(value = "Get a list of coupons")
    @GetMapping("/coupons")
    public List<Coupon> getCoupons(@RequestParam(required = false) String parametro) {

        return couponService.getCoupons(parametro);
    }
}
