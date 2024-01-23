package com.example.controlefinanceiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.controlefinanceiro.data.AccountCredentialVO;
import com.example.controlefinanceiro.data.PersonVO;
import com.example.controlefinanceiro.data.RegisterVO;
import com.example.controlefinanceiro.mapper.Mapper;
import com.example.controlefinanceiro.model.Person;
import com.example.controlefinanceiro.repository.PersonRepository;
import com.example.controlefinanceiro.security.JwtService; // Importe a classe correta

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PersonRepository repository;

    @Autowired
    private JwtService jwtService; 
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody @Valid AccountCredentialVO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = jwtService.generateToken((PersonVO) auth.getPrincipal());

        // Retornar o token na resposta
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterVO data) {
        if (this.repository.findByUserName(data.getLogin()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

        PersonVO newPerson = new PersonVO(data.getLogin(), encryptedPassword, data.getRole());

        Mapper.parseObject(repository.save(Mapper.parseObject(newPerson, Person.class)), PersonVO.class);

        return ResponseEntity.ok("User registered successfully");
    }
}
