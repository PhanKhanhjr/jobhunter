package jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jobhunter.domain.response.ResutlPaginationDTO;
import jobhunter.domain.Company;
import jobhunter.service.CompanyService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/v1")
@RestController
public class  CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> addCompany(@Valid @RequestBody Company company) {
        this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping("/companies")
    public ResponseEntity<ResutlPaginationDTO> fetchCompanies(
//            @RequestParam("current") Optional<String> currentOptional,
//            @RequestParam("pageSize") Optional<String> pageSizeOptional
            @Filter Specification<Company> spec, Pageable pageable)
    {
//        String currentPage =currentOptional.orElse("");
//        String pageSize =pageSizeOptional.orElse("");
//        Pageable pageable = PageRequest.of(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompanies(spec,pageable));
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Optional<Company>> findCompanyById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.companyService.getCompanyById(id));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable long id) {
        Optional<Company> company = this.companyService.getCompanyById(id);
        if (company == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
        }
        this.companyService.deleteCompanyById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Company deleted");
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        Company updateCompany = this.companyService.handleUpdateCompany(company);
        return ResponseEntity.ok(updateCompany);
    }
}
