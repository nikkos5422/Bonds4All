package cz.Bonds4All.controller;

import cz.Bonds4All.dto.UserHistory;
import cz.Bonds4All.dto.newUser;
import cz.Bonds4All.exceptionHandling.CustomException;
import cz.Bonds4All.repository.UserRepository;
import cz.Bonds4All.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping
    public Long createUser(@RequestBody newUser user) throws CustomException {
        return userService.createUser(user);
    }

    @GetMapping()
    public UserHistory getUserHistory(@RequestParam(value = "userId") Long id) throws CustomException {
        return userService.getUserHistory(id);
    }
}
