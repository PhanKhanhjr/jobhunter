package jobhunter.service;

import jobhunter.domain.Skill;
import jobhunter.domain.response.ResutlPaginationDTO;
import jobhunter.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Boolean isNameExist(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill createSkill(Skill s) {
        return this.skillRepository.save(s);
    }

    public Skill findSkillById(long id) {
        Optional<Skill> skill = this.skillRepository.findById(id);
        if (skill.isPresent()) {
            return skill.get();
        }
        return null;
    }

    public Skill updateSkill(Skill s) {
        return this.skillRepository.save(s);
    }

    public ResutlPaginationDTO fetchAllSkills(Specification <Skill> spec, Pageable pageable) {
        Page<Skill> pageUser = this.skillRepository.findAll(spec, pageable);
        ResutlPaginationDTO rs = new ResutlPaginationDTO();
        ResutlPaginationDTO.Meta meta = new ResutlPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());
        rs.setMeta(meta);
        rs.setResult(pageUser.getContent());
        return rs;
    }

    public void deleteSkill(long id) {
        Optional<Skill> skill = this.skillRepository.findById(id);
        Skill  currentSkil = skill.get();
        currentSkil.getJobs().forEach(job -> job.getSkills().remove(currentSkil));
        this.skillRepository.delete(currentSkil);
    }
}
