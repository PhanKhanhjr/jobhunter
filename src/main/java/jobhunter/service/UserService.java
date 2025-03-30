package jobhunter.service;

import jobhunter.DTO.Meta;
import jobhunter.DTO.ResutlPaginationDTO;
import jobhunter.DTO.UserResponseDTO;
import jobhunter.DTO.UserUpdateDTO;
import jobhunter.domain.User;
import jobhunter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

       List<UserResponseDTO> userList = users.getContent()
               .stream().map(item -> new UserResponseDTO(
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

    public User handleGetUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }
    public Boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public UserResponseDTO convertToUserCreateDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setCreatedAt(user.getCreatedAt());
        userResponseDTO.setGender(user.getGender());
        userResponseDTO.setAddress(user.getAddress());
        userResponseDTO.setAge(user.getAge());
        return userResponseDTO;
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
        return this.userRepository.save(currentUser);
    }

    public void updateRefreshToken(String token, String email) {
        User currentUser = this.handleGetUserByEmail(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

}

