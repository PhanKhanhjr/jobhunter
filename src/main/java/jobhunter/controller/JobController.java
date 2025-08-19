package jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jobhunter.domain.Job;
import jobhunter.domain.response.ResCreateJobDTO;
import jobhunter.domain.response.ResUpdateJobDTO;
import jobhunter.domain.response.ResultPaginationDTO;
import jobhunter.service.JobService;
import jobhunter.util.anotation.ApiMessage;
import jobhunter.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/v1")
@RestController
public class JobController {
    private final JobService jobService;
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("create a new job")
    public ResponseEntity<ResCreateJobDTO> create(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(job));
    }

    @PutMapping("/jobs")
    @ApiMessage("update a job")
    public ResponseEntity<ResUpdateJobDTO> update(@Valid @RequestBody Job job) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchById(job.getId());
        if (!currentJob.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(jobService.updateJob(job));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("delete a job")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchById(id);
        if (!currentJob.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        this.jobService.deleteById(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getJob(@PathVariable Long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchById(id);
        if (!currentJob.isPresent()) {
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.ok().body(currentJob.get());
    }

    @GetMapping("/jobs")
    @ApiMessage("get job with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllJob (@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.jobService.fetchAllJob(spec, pageable));
    }
}
