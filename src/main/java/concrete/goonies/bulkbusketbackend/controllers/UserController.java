package concrete.goonies.bulkbusketbackend.controllers;

import concrete.goonies.bulkbusketbackend.domain.HttpResponse;
import concrete.goonies.bulkbusketbackend.domain.User;
import concrete.goonies.bulkbusketbackend.dto.UserDTO;
import concrete.goonies.bulkbusketbackend.form.LoginForm;
import concrete.goonies.bulkbusketbackend.provider.TokenProvider;
import concrete.goonies.bulkbusketbackend.services.RoleService;
import concrete.goonies.bulkbusketbackend.services.UserPrincipal;
import concrete.goonies.bulkbusketbackend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static concrete.goonies.bulkbusketbackend.dto.mapper.UserDTOMapper.toUser;
import static java.time.LocalTime.now;
import static java.util.Map.of;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/26 05:11
 * Author: Scarra Luba
 */

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    private final TokenProvider tokenProvider;
    private final boolean isUsingMfa = true;

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {

        UserDTO user = userService.getUserByEmail(loginForm.getEmail());
        return isUsingMfa ? sendVerificationCode(user) : sendResponse(user);

    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO user) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user,
                                "access_token", tokenProvider.createAccessToken(getUserPrinciple(user)),
                                "refresh_token", tokenProvider.createRefreshToken(getUserPrinciple(user))))
                        .message("Login Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    private UserPrincipal getUserPrinciple(UserDTO user) {
        return  new UserPrincipal(toUser(userService.getUserByEmail(user.getEmail())),roleService.getRoleByUserId(user.getId()).getPermission());
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO user) {
        userService.sendVerificationCode(user);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user))
                        .message("Verification Code Sent")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user) {
        UserDTO userDTO = userService.createInsert(user);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userDTO))
                        .message("User created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email,@PathVariable("code") String code) {
        UserDTO user = userService.verifyCode(email,code);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user,
                                "access_token", tokenProvider.createAccessToken(getUserPrinciple(user)),
                                "refresh_token", tokenProvider.createRefreshToken(getUserPrinciple(user))))
                        .message("Login Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> getProfile(Authentication authentication) {
        UserDTO user = userService.getUserByEmail(authentication.getName());
        System.out.println(authentication.getName());
        System.out.println(authentication.getPrincipal());

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user))
                        .message("Profile Retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("user/get/<userId>").toUriString());
    }

}
