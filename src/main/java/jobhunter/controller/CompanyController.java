package jobhunter.controller;

import jakarta.validation.Valid;
import jobhunter.domain.Company;
import jobhunter.service.CompanyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyController {

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
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> cpnList = this.companyService.getAllCompanies();
        return ResponseEntity.status(HttpStatus.OK).body(cpnList);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> findCompanyById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.companyService.getCompanyById(id));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable long id) {
        Company company = this.companyService.getCompanyById(id);
        if (company == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
        }
        this.companyService.deleteCompanyById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Company deleted");
    }
}
