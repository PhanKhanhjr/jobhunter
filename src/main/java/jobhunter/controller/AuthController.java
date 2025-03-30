package jobhunter.controller;

import jakarta.validation.Valid;
import jobhunter.DTO.LoginDTO;
import jobhunter.DTO.ResLoginDTO;
import jobhunter.domain.User;
import jobhunter.service.UserService;
import jobhunter.util.SecutiryUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/api/v1")
@RestController
public class AuthController {

    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserService userService;
    private SecutiryUtil secutiryUtil;
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService, SecutiryUtil secutiryUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.secutiryUtil = secutiryUtil;
    }
    @Value("${phankhanh.jwt.refresh-token-validity-in-seconds}")
    private long refreshJwtExpiration;
    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        //impliments function UserDetailsService(trong nay co duy nhat 1 ham de load User => can viet ham de load User len)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //create token
        String access_token = this.secutiryUtil.createAccessToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setAccess_token(access_token);
        User currentUser = this.userService.handleGetUserByEmail(loginDTO.getUsername());
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getName());
        resLoginDTO.setUserlogin(userLogin);

        //create refresh_token
        String refress_token = this.secutiryUtil.createRefreshToke(loginDTO.getUsername(), resLoginDTO);

        //set refresh_toke
        this.userService.updateRefreshToken(refress_token,currentUser.getEmail());

        //set cookies
        ResponseCookie cooki = ResponseCookie
                .from("refresh_token", refress_token)
                .maxAge(refreshJwtExpiration)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cooki.toString())
                .body(resLoginDTO);
    }
}
