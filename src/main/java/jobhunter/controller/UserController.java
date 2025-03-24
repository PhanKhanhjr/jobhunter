package jobhunter.controller;

import jobhunter.DTO.ResutlPaginationDTO;
import jobhunter.domain.User;
import jobhunter.service.UserService;
import jobhunter.util.error.IdInvalidException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // ma hoa mat khau
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User khanhUser = userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(khanhUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if(id >100){
            throw new IdInvalidException("id phai nho hon 100");
        }
        this.userService.handleDeletUser(id);
//        return ResponseEntity.status(HttpStatus.OK).body("deleted user with id =" +id);
        // cach viet ngan gon hon
//        return ResponseEntity.ok( "deleted user with id = " + id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.userService.fetchUserById(id));
    }

    @GetMapping("/users")
    public  ResponseEntity<ResutlPaginationDTO> getAllUsers(@RequestParam("current") Optional<String> currentOptional,
                                                            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String currentPage =currentOptional.orElse("");
        String pageSize =pageSizeOptional.orElse("");
        Pageable pageable = PageRequest.of(Integer.parseInt(currentPage) -1, Integer.parseInt(pageSize));
        return ResponseEntity.ok(this.userService.fetchAllUser(pageable));
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleUpdateUser(user));
    }
}
