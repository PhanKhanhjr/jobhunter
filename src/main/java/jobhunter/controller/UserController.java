package jobhunter.controller;

import jobhunter.domain.User;
import jobhunter.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        User khanhUser = userService.handleCreateUser(user);
        return khanhUser;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeletUser(id);
        return "delete user with id = " + id;
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return this.userService.fetchUserById(id);
    }

    @GetMapping("/user")
    public List<User> getAllUsers(){
        return this.userService.fetchAllUser();
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {
        return this.userService.handleUpdateUser(user);
    }
}
