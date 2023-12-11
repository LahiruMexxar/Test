package com.Mexxar.Test.Controller;

import com.Mexxar.Test.DTO.AuthResponse;
import com.Mexxar.Test.Model.AuthRequest;
import com.Mexxar.Test.Model.RegRequest;
import com.Mexxar.Test.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService service;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse>register(@RequestBody AuthRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
