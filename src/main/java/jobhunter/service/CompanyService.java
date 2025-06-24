package jobhunter.service;

import jakarta.persistence.EntityNotFoundException;
import jobhunter.domain.User;
import jobhunter.domain.response.ResutlPaginationDTO;
import jobhunter.domain.Company;
import jobhunter.repository.CompanyRepository;
import jobhunter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResutlPaginationDTO fetchAllCompanies(Specification<Company> spec,Pageable pageable) {
        Page<Company> companies =this.companyRepository.findAll(spec,pageable);
        ResutlPaginationDTO resutlPaginationDTO = new ResutlPaginationDTO();
        ResutlPaginationDTO.Meta meta = new ResutlPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(companies.getTotalPages());
        meta.setTotal(companies.getTotalElements());

        resutlPaginationDTO.setMeta(meta);
        resutlPaginationDTO.setResult(companies.getContent());
        return resutlPaginationDTO;
    }

    public Optional<Company> getCompanyById(long id) {
        return this.companyRepository.findById(id)
                .or(() -> {
                    throw new EntityNotFoundException("Company not found with id: " + id);
                });
    }
    public void deleteCompanyById(long id) {
        Optional<Company> companyOptional= this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company com = companyOptional.get();
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }
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
