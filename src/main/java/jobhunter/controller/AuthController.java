package jobhunter.controller;

import jakarta.validation.Valid;
import jobhunter.domain.request.ReqLoginDTO;
import jobhunter.domain.response.ResLoginDTO;
import jobhunter.domain.User;
import jobhunter.service.UserService;
import jobhunter.util.SecurityUtil;
import jobhunter.util.anotation.ApiMessage;
import jobhunter.util.error.IdInvalidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
public class AuthController {

    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserService userService;
    private SecurityUtil securityUtil;
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService, SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.securityUtil = securityUtil;
    }
    @Value("${phankhanh.jwt.refresh-token-validity-in-seconds}")
    private long refreshJwtExpiration;
    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        //impliments function UserDetailsService(trong nay co duy nhat 1 ham de load User => can viet ham de load User len)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // set inf user login to context (maybe used later)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User currentUser = this.userService.handleGetUserByEmail(loginDTO.getUsername());
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getName());
        resLoginDTO.setUser(userLogin);

        //create access_token
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO.getUser());


        resLoginDTO.setAccessToken(access_token);
        //create refresh_token
        String refress_token = this.securityUtil.createRefreshToke(loginDTO.getUsername(), resLoginDTO);

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

    @GetMapping("/auth/account")
    @ApiMessage("fetch account success")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser = this.userService.handleGetUserByEmail(email);
        ResLoginDTO.UserLogin resLoginDTO = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount resGetAccount = new ResLoginDTO.UserGetAccount();
        if (currentUser != null) {
            resLoginDTO.setId(currentUser.getId());
            resLoginDTO.setEmail(currentUser.getEmail());
            resLoginDTO.setName(currentUser.getName());
            resGetAccount.setUser(resLoginDTO);
        }
        return ResponseEntity.ok(resGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get User by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(@CookieValue(name = "refresh_token", defaultValue = "abcxyz") String refreshToken) throws IdInvalidException {
        if (refreshToken.equals("abcxyz")) {
            throw new IdInvalidException("You don't have a refresh token in your cookies");
        }
        //check valid token
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();
        //check user by token and email
        User currentUser = this.userService.fetchUserByTokenAndEmail(refreshToken, email);
        if (currentUser == null) {
            throw new IdInvalidException("Refresh token invalid");
        }
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByEmail(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(), currentUserDB.getName());
        resLoginDTO.setUser(userLogin);

        //create access_token
        String access_token = this.securityUtil.createAccessToken(email, resLoginDTO.getUser());


        resLoginDTO.setAccessToken(access_token);
        //create refresh_token
        String new_refress_token = this.securityUtil.createRefreshToke(email, resLoginDTO);

        //set refresh_toke
        this.userService.updateRefreshToken(new_refress_token,currentUserDB.getEmail());

        //set cookies
        ResponseCookie cooki = ResponseCookie
                .from("refresh_token", new_refress_token)
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

    @PostMapping("/auth/logout")
    @ApiMessage("Logout success")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        //get email from spring security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
//        String email2 = SecutiryUtil.getCurrentUserLogin().isPresent() ? SecutiryUtil.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("Access token invalid");
        }

        this.userService.updateRefreshToken(null,email);
        ResponseCookie deleteCookies = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookies.toString()).build();
    }
}
