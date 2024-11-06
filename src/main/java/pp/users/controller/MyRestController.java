package pp.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pp.users.model.User;
import pp.users.service.UserService;

@RestController
public class MyRestController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public MyRestController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        String userPassBeforeEncoding = user.getPassword();
        String encodedPass = passwordEncoder.encode(userPassBeforeEncoding);
        user.setPassword(encodedPass);
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PatchMapping("/users/{username}")
    public void disableEnableUserByUsername(@PathVariable String username,
                                            @RequestHeader String patch_type) {
        if (patch_type.equals("disable")) {
            userService.disableUserByUsername(username);
        } else if (patch_type.equals("enable")) {
            userService.enableUserByUsername(username);
        }
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user, @RequestHeader String password_change) {
        if (Boolean.parseBoolean(password_change)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{username}")
    public void deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
    }
}
