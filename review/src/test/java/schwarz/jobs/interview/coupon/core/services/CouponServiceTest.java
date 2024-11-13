package schwarz.jobs.interview.coupon.core.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@ExtendWith(SpringExtension.class)
public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;
    
    // Methods to avoid repetitive code in tests
    private Coupon createCoupon(String code, BigDecimal discount, BigDecimal minBasketValue) {
        return Coupon.builder()
            .code(code)
            .discount(discount)
            .minBasketValue(minBasketValue)
            .build();
    }

    private Basket createBasket(BigDecimal value) {
        return Basket.builder()
            .value(value)
            .build();
    }

    @Test
    public void should_create_coupon() {
        CouponDTO dto = CouponDTO.builder()
            .code("12345")
            .discount(BigDecimal.TEN)
            .minBasketValue(BigDecimal.valueOf(50))
            .build();

        couponService.createCoupon(dto);

        verify(couponRepository, times(1)).save(any());
    }
    
    // I separated the tests with more descriptive method names, also for catch errors more easly
    @Test
    public void should_apply_coupon_when_basket_value_is_positive() {
        Basket basket = createBasket(BigDecimal.valueOf(100));

        when(couponRepository.findByCode("1111")).thenReturn(Optional.of(createCoupon("1111", BigDecimal.TEN, BigDecimal.valueOf(50))));

        Optional<Basket> result = couponService.apply(basket, "1111");

        assertThat(result).hasValueSatisfying(b -> {
            assertThat(b.getAppliedDiscount()).isEqualTo(BigDecimal.TEN);
            assertThat(b.isApplicationSuccessful()).isTrue();
        });
    }

    @Test
    public void should_return_same_basket_when_value_is_zero() {
        Basket basket = createBasket(BigDecimal.valueOf(0));

        when(couponRepository.findByCode("1111")).thenReturn(Optional.of(createCoupon("1111", BigDecimal.TEN, BigDecimal.valueOf(50))));

        Optional<Basket> result = couponService.apply(basket, "1111");

        assertThat(result).hasValueSatisfying(b -> {
            assertThat(b).isEqualTo(basket);
            assertThat(b.isApplicationSuccessful()).isFalse();
        });
    }

    @Test
    public void should_throw_exception_when_basket_value_is_negative() {
        Basket basket = createBasket(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> couponService.apply(basket, "1111"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Can't apply negative discounts");
    }

//    @Test
//    public void test_apply_coupon_method() {
//
//        final Basket firstBasket = Basket.builder()
//            .value(BigDecimal.valueOf(100))
//            .build();
//
//        when(couponRepository.findByCode("1111")).thenReturn(Optional.of(Coupon.builder()
//            .code("1111")
//            .discount(BigDecimal.TEN)
//            .minBasketValue(BigDecimal.valueOf(50))
//            .build()));
//
//        Optional<Basket> optionalBasket = couponService.apply(firstBasket, "1111");
//
//        assertThat(optionalBasket).hasValueSatisfying(b -> {
//            assertThat(b.getAppliedDiscount()).isEqualTo(BigDecimal.TEN);
//            assertThat(b.isApplicationSuccessful()).isTrue();
//        });
//
//        final Basket secondBasket = Basket.builder()
//            .value(BigDecimal.valueOf(0))
//            .build();
//
//        optionalBasket = couponService.apply(secondBasket, "1111");
//
//        assertThat(optionalBasket).hasValueSatisfying(b -> {
//            assertThat(b).isEqualTo(secondBasket);
//            assertThat(b.isApplicationSuccessful()).isFalse();
//        });
//
//        final Basket thirdBasket = Basket.builder()
//            .value(BigDecimal.valueOf(-1))
//            .build();
//
//        assertThatThrownBy(() -> {
//            couponService.apply(thirdBasket, "1111");
//        }).isInstanceOf(RuntimeException.class)
//            .hasMessage("Can't apply negative discounts");
//    }

    @Test
    public void should_return_list_of_coupons_when_valid_codes_are_given() {

        CouponRequestDTO dto = CouponRequestDTO.builder()
            .codes(Arrays.asList("1111", "1234"))
            .build();

        when(couponRepository.findByCode("1111"))
        		.thenReturn(Optional.of(createCoupon("1111", BigDecimal.TEN, BigDecimal.valueOf(50))));
        when(couponRepository.findByCode("1234"))
        		.thenReturn(Optional.of(createCoupon("1234", BigDecimal.TEN, BigDecimal.valueOf(50))));


        List<Coupon> returnedCoupons = couponService.getCoupons(dto);

        assertThat(returnedCoupons.get(0).getCode()).isEqualTo("1111");
        assertThat(returnedCoupons.get(1).getCode()).isEqualTo("1234");
    }
}
