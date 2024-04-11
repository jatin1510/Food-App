package com.jatin.controller;

import com.jatin.config.JwtProvider;
import com.jatin.model.Cart;
import com.jatin.model.USER_ROLE;
import com.jatin.model.User;
import com.jatin.repository.CartRepository;
import com.jatin.repository.UserRepository;
import com.jatin.request.ForgotPasswordRequest;
import com.jatin.request.GoogleLoginRequest;
import com.jatin.request.LoginRequest;
import com.jatin.request.ResetPasswordRequest;
import com.jatin.response.AuthResponse;
import com.jatin.response.MessageResponse;
import com.jatin.service.CustomUserDetailsService;
import com.jatin.service.EmailSenderService;
import com.jatin.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private TokenService tokenService;

    @Value("${spring.mail.username}")
    private String email;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new Exception("Email is already used...");
        }

        User createUser = new User();
        createUser.setEmail(user.getEmail());
        createUser.setFullName(user.getFullName());
        createUser.setRole(user.getRole());
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(createUser);

        Cart cart = new Cart();
        cart.setCustomer(savedUser);

        if (user.getRole() == USER_ROLE.ROLE_CUSTOMER) {
            cartRepository.save(cart);
        }


//        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getMail(), user.getPassword());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = jwtProvider.generateToken(authentication);
//
//        AuthResponse authResponse = new AuthResponse();
//        authResponse.setJwt(jwt);
//        authResponse.setMessage("Register Success");
//        authResponse.setRole(savedUser.getRole());
//        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);


//        LoginRequest req = new LoginRequest();
//        req.setMail(savedUser.getMail());
//        req.setPassword(user.getPassword());
//        return new ResponseEntity<>(signin(req).getBody(),HttpStatus.CREATED);

        USER_ROLE role = user.getRole();
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(role.toString()));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Signup Success");
        authResponse.setRole(savedUser.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/google-signin")
    public ResponseEntity<AuthResponse> googleSignin(@RequestBody GoogleLoginRequest request) throws Exception{
        String username = request.getEmail();

        User isEmailExist = userRepository.findByEmail(username);
        if (isEmailExist == null) {
            User user = new User();
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());
            user.setRole(USER_ROLE.ROLE_CUSTOMER);
            return createUserHandler(user);
        }

        Authentication authentication = googleAuthenticate(username);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login success");
        authResponse.setRole(USER_ROLE.valueOf(role));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication googleAuthenticate(String username) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest request) throws Exception {
        String username = request.getEmail();
        String password = request.getPassword();

        User isEmailExist = userRepository.findByEmail(username);
        if (isEmailExist == null) {
            throw new Exception("User does not exist");
        }

        Authentication authentication = authenticate(username, password);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login success");
        authResponse.setRole(USER_ROLE.valueOf(role));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @PostMapping("/forgot-password")
    private ResponseEntity<MessageResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) throws Exception {
        String to = request.getEmail();
        User isEmailExist = userRepository.findByEmail(to);
        if (isEmailExist == null) {
            return new ResponseEntity<>(new MessageResponse("User does not exist"), HttpStatus.BAD_REQUEST);
        }

        String token = tokenService.generateToken(to);
        String subject = "Reset Your Password";
        String htmlContent = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>Password Reset</title>"
                + "</head>"
                + "<body>"
                + "<h2>Password Reset Request</h2>"
                + "<p>We received a request to reset the password associated with this email address. If you made this request, please click the link below to reset your password:</p>"
                + "<p><a href=\"http://localhost:3000/reset-password?token=" + token + "\">Reset Password</a></p>"
                + "<p>The link will expire after 10 minutes.</p>"
                + "<p>If you did not make this request, you can safely ignore this email.</p>"
                + "<p>Thank you!</p>"
                + "</body>"
                + "</html>";

        emailSenderService.sendEmail(to, subject, htmlContent);
        return new ResponseEntity<>(new MessageResponse("Mail sent to your account"), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    private ResponseEntity<MessageResponse> resetPassword(@RequestBody ResetPasswordRequest request) throws Exception {
        String uId = request.getToken();
        boolean isVerified = tokenService.verifyToken(uId);
        if (!isVerified) {
            return new ResponseEntity<>(new MessageResponse("Token Expired"), HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findByEmail(tokenService.getEmail(uId));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        // Reset Password Login
        tokenService.removeToken(uId);
        return new ResponseEntity<>(new MessageResponse("Password reset successfully"), HttpStatus.OK);
    }
}