package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping(value = "/test", produces = "application/json")
    public String getTest() {
        return "HelloTest!";
    }

    @GetMapping(value = "/users", produces = "application/json")
    public ResponseEntity<Object> getAllUsers() {
        return new ResponseEntity<>(userRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}", produces = "application/json")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws Exception {
        Optional<User> user = Optional.ofNullable(userRepo.findUserById(userId));
        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping(value = "/user/{name}", produces = "application/json")
    public ResponseEntity<List<User>> getUserByName(@PathVariable(value = "name") String username) throws Exception {
        List<User> users = userRepo.findUserByUsername(username);
        return users.isEmpty() ? ResponseEntity.ok().body(users) : ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/users", consumes = "application/json")
    public User createUser(@RequestBody User user) {
        return userRepo.save(user);
    }

//    @PutMapping("/users/{id}")
//    public ResponseEntity <User> updateUser(@PathVariable(value = "id") Long userId, @RequestBody User userData) throws ResourceNotFoundException {
//        User user = userRepo.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
//
//        user.setFirstName(userData.getFirstName());
//        final User updatedUser = userRepo.save(user);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//    @DeleteMapping("/users/{id}")
//    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
//        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
//        userRepo.delete(user);
//        Map <String, Boolean> response = new HashMap< >();
//        response.put("deleted", Boolean.TRUE);
//        return response;
//    }
}