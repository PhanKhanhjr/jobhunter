package jobhunter.service;

import jobhunter.DTO.Meta;
import jobhunter.DTO.ResutlPaginationDTO;
import jobhunter.domain.User;
import jobhunter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User handleCreateUser(User user) {
       return this.userRepository.save(user);
    }

    public void handleDeletUser(long id) {
            this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }
    public ResutlPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
       Page<User> users = this.userRepository.findAll(spec, pageable);
       ResutlPaginationDTO paginationDTO = new ResutlPaginationDTO();
       Meta meta = new Meta();
       meta.setPage(pageable.getPageNumber()+1);
       meta.setPageSize(pageable.getPageSize());
       meta.setPages(users.getTotalPages());
       meta.setTotal(users.getTotalElements());
       paginationDTO.setMeta(meta);
       paginationDTO.setResult(users.getContent());
        return paginationDTO;
    }

    public User handleUpdateUser(User user) {
        User updateUser = this.fetchUserById(user.getId());
        if (updateUser != null) {
            updateUser.setName(user.getName());
            updateUser.setEmail(user.getEmail());
            updateUser.setPassword(user.getPassword());
            this.userRepository.save(updateUser);
            this.userRepository.save(updateUser);
        }
        return updateUser;
    }

    public User handleGetUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

}
