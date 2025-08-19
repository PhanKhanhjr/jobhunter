package jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jobhunter.domain.Skill;
import jobhunter.domain.response.ResultPaginationDTO;
import jobhunter.service.SkillService;
import jobhunter.util.anotation.ApiMessage;
import jobhunter.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create skill success")
    public ResponseEntity<Skill> create (@Valid @RequestBody Skill s) throws IdInvalidException {
            //checkname
        if (s.getName()!=null & this.skillService.isNameExist(s.getName())) {
        throw new IdInvalidException("Skill " + s.getName() + " already exist");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createSkill(s));
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> update (@Valid @RequestBody Skill s) throws IdInvalidException {
        Skill currentSkill = this.skillService.findSkillById(s.getId());
        if (currentSkill == null) {
            throw new IdInvalidException("Skill " + s.getId() + " does not exist");
        }

        if (s.getName()!=null & !currentSkill.getName().equals(s.getName())) {
            throw new IdInvalidException("Skill " + s.getName() + " already exist");
        }
        currentSkill.setName(s.getName());
        return ResponseEntity.ok().body(this.skillService.updateSkill(currentSkill));
    }

    @GetMapping("/skills")
    @ApiMessage("fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.fetchAllSkills(spec, pageable));
    }

    @DeleteMapping("skills/{id}")
    @ApiMessage("delete a skill")
    public ResponseEntity<Void> delete (@PathVariable long id) throws IdInvalidException {
        Skill skill = this.skillService.findSkillById(id);
        if (skill == null) {
            throw new IdInvalidException("Skill " + id + " does not exist");
        }
        this.skillService.deleteSkill(id);
        return ResponseEntity.ok().body(null);
    }

}
