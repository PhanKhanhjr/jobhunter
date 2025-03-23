package jobhunter.service;

import jakarta.persistence.EntityNotFoundException;
import jobhunter.domain.Company;
import jobhunter.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        List<Company> companies =this.companyRepository.findAll();
        return companies.isEmpty() ? Collections.emptyList() : companies;
    }

    public Company getCompanyById(long id) {
        return this.companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company not found"));
    }
    public void deleteCompanyById(long id) {
        this.companyRepository.deleteById(id);
    }

    public Company handleUpdateCompany(Company company) {
        Optional<Company> companyOptional = this.companyRepository.findById(company.getId());
        if (companyOptional.isPresent()) {
            Company companyToUpdate = companyOptional.get();
            companyToUpdate.setName(company.getName());
            companyToUpdate.setAddress(company.getAddress());
            companyToUpdate.setDescription(company.getDescription());
            companyToUpdate.setLogo(company.getLogo());
            return this.companyRepository.save(companyToUpdate);
        }
        return null;
    }
}
