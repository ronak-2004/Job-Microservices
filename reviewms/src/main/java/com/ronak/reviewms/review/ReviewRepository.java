package com.ronak.reviewms.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByCompanyId(Long companyId);
}

/*
findBy CompanyId
where CompanyId is field in review table
or you can think like
findByCompanyId => findBy review.company.id; ;)
Runtime query : ->
SELECT * FROM review WHERE company_id = ?;

reviewRepository.findByCompanyId(1L)
SELECT * FROM review WHERE company_id = 1;
*/
