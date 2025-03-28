package jobhunter.service;

import jobhunter.DTO.Meta;
import jobhunter.DTO.ResutlPaginationDTO;
import jobhunter.DTO.UserCreateDTO;
import jobhunter.DTO.UserUpdateDTO;
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
import java.util.stream.Collectors;

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

       List<UserCreateDTO> userList = users.getContent()
               .stream().map(item -> new UserCreateDTO(
               item.getId(),
               item.getName(),
               item.getEmail(),
               item.getAge(),
               item.getGender(),
               item.getAddress(),
               item.getUpdatedAt(),
               item.getCreatedAt())
               )
               .collect(Collectors.toList());
        paginationDTO.setResult(userList);
        return paginationDTO;
    }

//    public User handleUpdateUser(User user) {
//        User updateUser = this.fetchUserById(user.getId());
//        if (updateUser != null) {
//            updateUser.setName(user.getName());
//            updateUser.setEmail(user.getEmail());
//            updateUser.setPassword(user.getPassword());
//            this.userRepository.save(updateUser);
//            this.userRepository.save(updateUser);
//        }
//        return updateUser;
//    }

    public User handleGetUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }
    public Boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public UserCreateDTO convertToUserCreateDTO(User user) {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setId(user.getId());
        userCreateDTO.setName(user.getName());
        userCreateDTO.setEmail(user.getEmail());
        userCreateDTO.setCreatedAt(user.getCreatedAt());
        userCreateDTO.setGender(user.getGender());
        userCreateDTO.setAddress(user.getAddress());
        userCreateDTO.setAge(user.getAge());
        return userCreateDTO;
    }

    public UserUpdateDTO convertToUserUpdateDTO(User user) {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setId(user.getId());
        userUpdateDTO.setName(user.getName());
        userUpdateDTO.setEmail(user.getEmail());
        userUpdateDTO.setGender(user.getGender());
        userUpdateDTO.setAddress(user.getAddress());
        userUpdateDTO.setAge(user.getAge());
        userUpdateDTO.setUpdatedAt(user.getUpdatedAt());
        userUpdateDTO.setUpdatedBy(user.getUpdatedBy());
        return userUpdateDTO;
    }

    public User handleUpdateUser(User user) {
        User currentUser = this.fetchUserById(user.getId());
        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());
        currentUser.setGender(user.getGender());
        currentUser.setAddress(user.getAddress());
        currentUser.setUpdatedAt(user.getUpdatedAt());
        currentUser.setUpdatedBy(user.getUpdatedBy());
        return this.userRepository.save(currentUser);
    }

}

