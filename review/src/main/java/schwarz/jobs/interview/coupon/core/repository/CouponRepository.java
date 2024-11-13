package schwarz.jobs.interview.coupon.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import schwarz.jobs.interview.coupon.core.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

	// you can add a @query anotation if you have complex queries, 
	// but the method name follows the query method conventions, 
    Optional<Coupon> findByCode(final String code);

}
