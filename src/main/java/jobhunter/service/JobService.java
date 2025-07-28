package jobhunter.service;

import jobhunter.domain.Job;
import jobhunter.domain.Skill;
import jobhunter.domain.response.ResCreateJobDTO;
import jobhunter.domain.response.ResUpdateJobDTO;
import jobhunter.domain.response.ResutlPaginationDTO;
import jobhunter.repository.JobRepository;
import jobhunter.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    final JobRepository jobRepository;
    final SkillRepository skillRepository;
    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public ResCreateJobDTO createJob(Job job) {
        if(job.getSkills() != null) {
            List<Long> reqSkill = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSKill = this.skillRepository.findByIdIn(reqSkill);
            job.setSkills(dbSKill);
        }
        Job currentJob = this.jobRepository.save(job);

        //convert
        ResCreateJobDTO dto = new ResCreateJobDTO();
        dto.setId(currentJob.getId());
        dto.setJobName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.getActive());
        dto.setCreateAt(currentJob.getCreatedAt());
        dto.setCreateBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skill = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skill);
        }
        return dto;
    }

    public ResUpdateJobDTO updateJob(Job job) {
        if(job.getSkills() != null) {
            List<Long> reqSkill = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSKill = this.skillRepository.findByIdIn(reqSkill);
            job.setSkills(dbSKill);
        }
        Job currentJob = this.jobRepository.save(job);
        //convert
        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(currentJob.getId());
        dto.setJobName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.getActive());
        dto.setUpdateAt(currentJob.getUpdatedAt());
        dto.setUpdateBy(currentJob.getUpdatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skill = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skill);
        }
        return dto;
    }

  public Optional<Job> fetchById(Long id) {
        return this.jobRepository.findById(id);
  }

  public void deleteById(Long id) {
        this.jobRepository.deleteById(id);
  }

  public ResutlPaginationDTO fetchAllJob(Specification<Job> spec, Pageable pageable) {
      Page<Job> page = this.jobRepository.findAll(spec, pageable);
      ResutlPaginationDTO rs = new ResutlPaginationDTO();
      ResutlPaginationDTO.Meta mt = new ResutlPaginationDTO.Meta();

      mt.setPage(pageable.getPageNumber() +1);
      mt.setPages(pageable.getPageSize());
      mt.setPages(page.getTotalPages());
      mt.setTotal(page.getTotalElements());
      rs.setMeta(mt);
      rs.setResult(page.getContent());
      return rs;
  }
}
