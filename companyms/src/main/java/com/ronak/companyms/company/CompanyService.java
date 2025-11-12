package com.ronak.companyms.company;

import com.ronak.companyms.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {
    void createCompany(Company company);
    Company getCompanyById(Long id);
    List<Company> getAllCompanies();
    boolean updateCompany(Company company, Long id);
    boolean deleteCompanyById(Long id);
    public void updateCompanyRating(ReviewMessage reviewMessage);
}
