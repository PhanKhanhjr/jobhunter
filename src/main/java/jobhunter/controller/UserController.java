package jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jobhunter.DTO.ResutlPaginationDTO;
import jobhunter.DTO.UserCreateDTO;
import jobhunter.DTO.UserUpdateDTO;
import jobhunter.domain.User;
import jobhunter.service.UserService;
import jobhunter.util.anotation.ApiMessage;
import jobhunter.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/users")
    @ApiMessage("Create user success")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) throws IdInvalidException {
        // ma hoa mat khau
        if(this.userService.isEmailExist(user.getEmail())) {
         throw new IdInvalidException("Email" + user.getEmail() + " this email is already use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToUserCreateDTO(user));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete User success")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if(this.userService.fetchUserById(id) == null) {
            throw new IdInvalidException("User with the specified ID was not found");
        }
        this.userService.handleDeletUser(id);
//        return ResponseEntity.status(HttpStatus.OK).body("deleted user with id =" +id);
        // cach viet ngan gon hon
//        return ResponseEntity.ok( "deleted user with id = " + id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch user by ID")
    public ResponseEntity<UserCreateDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User user = this.userService.fetchUserById(id);
        if(user == null) {
            throw new IdInvalidException("User with the specified ID was not found");
        }
        return ResponseEntity.ok(this.userService.convertToUserCreateDTO(user));
    }

    @GetMapping("/users")
    @ApiMessage("Get user success")
    public  ResponseEntity<ResutlPaginationDTO> getAllUsers(
//            @RequestParam("current") Optional<String> currentOptional,
//            @RequestParam("pageSize") Optional<String> pageSizeOptional
            @Filter Specification<User> spec, Pageable pageable
            ) {
//        String currentPage =currentOptional.orElse("");
//        String pageSize =pageSizeOptional.orElse("");
//        Pageable pageable = PageRequest.of(Integer.parseInt(currentPage) -1, Integer.parseInt(pageSize));
        return ResponseEntity.ok(this.userService.fetchAllUser(spec, pageable));
    }

    @PutMapping("/users")
    @ApiMessage("Information has been updated")
    public ResponseEntity<UserUpdateDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        if(this.userService.fetchUserById(user.getId()) == null) {
            throw new IdInvalidException("No user found with the provided email address: " + user.getEmail());
        }
        User updateUser = this.userService.handleUpdateUser(user);
        this.userService.handleCreateUser(updateUser);
        this.userService.convertToUserUpdateDTO(updateUser);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToUserUpdateDTO(updateUser));
    }
}
