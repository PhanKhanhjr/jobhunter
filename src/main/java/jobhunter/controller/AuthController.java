package jobhunter.controller;

import jobhunter.DTO.LoginDTO;
import jobhunter.domain.User;
import jobhunter.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserService userService;
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }
    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody LoginDTO loginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        //impliments function UserDetailsService(trong nay co duy nhat 1 ham de load User => can viet ham de load User len)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return ResponseEntity.ok().body(loginDTO);
    }
}
