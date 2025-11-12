package com.ronak.jobms.job.impl;

import com.ronak.jobms.job.Job;
import com.ronak.jobms.job.JobRepository;
import com.ronak.jobms.job.JobService;
import com.ronak.jobms.job.clients.CompanyClient;
import com.ronak.jobms.job.clients.ReviewClient;
import com.ronak.jobms.job.dto.JobDTO;
import com.ronak.jobms.job.external.Company;
import com.ronak.jobms.job.external.Review;
import com.ronak.jobms.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

//    private List<Job> jobs = new ArrayList<>();
    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;

    private CompanyClient companyClient;
    private ReviewClient reviewClient;

    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

//    private Long nextId=1L;

    @Override
    public List<JobDTO> findAll() {

        List<Job> jobs = jobRepository.findAll();
        //List<JobWithCompanyDTO> jobWithCompanyDTOs = new ArrayList<>();

//        for(Job job : jobs){
//            JobWithCompanyDTO jobWithCompanyDTO =convertToDTO(job);
//
//            jobWithCompanyDTOs.add(jobWithCompanyDTO);
//        }
        //use streams
        return jobs.stream().map(this::convertToDTO)
                .collect(Collectors.toList());

        //return jobRepository.findAll();
    }

    private JobDTO convertToDTO(Job job) {
        if(job == null) return null;
        //RestTemplate restTemplate = new RestTemplate();
        // Using "http://localhost:8081/companies/"
        // Using "http://COMPANYMS:8081/companies/" name of service
        //Company company = restTemplate.getForObject("http://COMPANYMS:8081/companies/"+job.getCompanyId(),Company.class);
        Company company = companyClient.getCompany(job.getCompanyId());
//        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
//                "http://REVIEWMS:8083/reviews?companyId="+company.getId(),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Review>>(){});
//        List<Review> reviews = reviewResponse.getBody();
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
        return JobMapper.mapToJobWithCompanyDto(job,company,reviews) ;
    }

    @Override
    public void createJob(Job job) {
//        job.setId(nextId++);
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        return convertToDTO(job);
    }

    @Override
    public boolean deleteJobById(Long id) {
        try{
            jobRepository.deleteById(id);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if(jobOptional.isPresent()){
            Job job = jobOptional.get();
            if (updatedJob.getTitle() != null) job.setTitle(updatedJob.getTitle());
            if (updatedJob.getMaxSalary() != null) job.setMaxSalary(updatedJob.getMaxSalary());
            if (updatedJob.getMinSalary() != null) job.setMinSalary(updatedJob.getMinSalary());
            if (updatedJob.getLocation() != null) job.setLocation(updatedJob.getLocation());
            jobRepository.save(job);
            return true;
        }
        return false;
    }
}