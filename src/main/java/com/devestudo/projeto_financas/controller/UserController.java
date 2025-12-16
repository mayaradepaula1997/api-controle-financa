package com.devestudo.projeto_financas.controller;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.CreateUserDto;
import com.devestudo.projeto_financas.entities.dtos.UpdateUser;
import com.devestudo.projeto_financas.entities.dtos.UserDto;
import com.devestudo.projeto_financas.entities.dtos.UserResponseDto;
import com.devestudo.projeto_financas.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody CreateUserDto createUserDto){

        User user = userService.createUser(createUserDto);

        UserResponseDto response = new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getSalary(),
                user.getRole().name(),
                user.getPassword()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // retorna o código HTTP  201 CREATED
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") //Apenas o usuario de tiver a role ADMIN, vai poder listar
    public ResponseEntity <List<UserDto>> listUser(){

        List<UserDto> userDtos = userService.listUser();

        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> listById(@PathVariable Long id){

        if (id < 0){
            return ResponseEntity.badRequest().build();
        }

        UserDto userDto = userService.findById(id);

        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){

        userService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUser updateUser){

        UserDto userUpdate = userService.updateUser(id, updateUser);

        return ResponseEntity.ok(userUpdate);
    }

}
