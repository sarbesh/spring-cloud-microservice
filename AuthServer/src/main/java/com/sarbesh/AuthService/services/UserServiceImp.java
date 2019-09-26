package com.sarbesh.AuthService.services;

import com.sarbesh.AuthService.model.LoginViewModel;
import com.sarbesh.AuthService.model.User;
import com.sarbesh.AuthService.repository.UserRepository;
import com.sarbesh.AuthService.security.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.System.*;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private Authentication auth;

    @Override
    @Transactional
    public String loginUser(LoginViewModel loginModel) {
        String userName = loginModel.getUserName();
        String password = loginModel.getPassword();
        User usr;
        if (!userName.contains("@")) {
            long id = Integer.parseInt(userName);
            usr = userRepo.findById(id).orElse(null);
        } else {
            usr = userRepo.findByEmail(userName).orElse(null);
        }
        String result;
        if (usr == null) {
//            return "Employee Not Found";
            throw new RuntimeException("Employee Not Found");
        } else if (password.equals(usr.getPassword())) {
            String token = auth.generateToken(usr);
            result = token;
        } else {
            throw new RuntimeException("Incorrect User or Password");
        }
        return result;
    }

    @Override
    public void logoutUser(String token) {

    }

    @Override
    public boolean checkLogin(String token) {
        return false;
    }
}
