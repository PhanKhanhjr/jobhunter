package jobhunter.service;

import jobhunter.domain.User;
import jobhunter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public List<User> fetchAllUser(){
        return this.userRepository.findAll();
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

}
