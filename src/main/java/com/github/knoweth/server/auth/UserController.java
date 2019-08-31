package com.github.knoweth.server.auth;

import com.github.knoweth.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller to login, register, and view user status.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Register a new user.
     */
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response register(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Response.err("User already exists");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.info("Adding user: " + user.toString());
            userRepository.save(user);
            return Response.success("Success!");
        }
    }

    /**
     * Login as a user.
     */
    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    /**
     * Dummy endpoint used by frontend to check if user is logged in
     */
    @GetMapping(value = {"/status"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public User status() {
        return UserUtils.getUser();
    }
}