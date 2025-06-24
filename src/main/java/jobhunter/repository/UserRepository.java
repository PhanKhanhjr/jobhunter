package jobhunter.repository;

import jobhunter.domain.Company;
import jobhunter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findUserByEmail(String email);
    Boolean existsByEmail(String email);
    User findUserByRefreshTokenAndEmail(String token, String email);

    List<User> findByCompany(Company com);
}
