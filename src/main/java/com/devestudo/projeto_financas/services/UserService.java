package com.devestudo.projeto_financas.services;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.request.CreateUserDto;
import com.devestudo.projeto_financas.entities.dtos.request.UpdateUser;
import com.devestudo.projeto_financas.entities.dtos.request.UserDto;
import com.devestudo.projeto_financas.enums.RoleEnum;
import com.devestudo.projeto_financas.exception.BusinessException;
import com.devestudo.projeto_financas.exception.ResourceNotFoundException;
import com.devestudo.projeto_financas.repository.CategoryRepository;
import com.devestudo.projeto_financas.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private UserRepository userRepository;

    private CategoryRepository categoryRepository;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CategoryRepository categoryRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(CreateUserDto userDto) {

        var userExiste = userRepository.findByEmail(userDto.email());

        System.out.println(userExiste);

        if (userExiste.isPresent()) {
            throw new BusinessException("Cliente já existe");
        }
        String passwordHash = passwordEncoder.encode(userDto.password()); //Chamando o método que vai faer a criptografia da senha

        User user = new User(
                userDto.name(),
                userDto.email(),
                passwordHash,    // senha criptografada
                userDto.salary(),
                RoleEnum.USER
        );

        return userRepository.save(user);
    }

    //Método vai retornar os usuários em formato de paginação
    public Page<UserDto> listUser(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        return userRepository.findAll(pageable) //findAll - para paginação

                .map(user -> new UserDto(    //map: transforma cada User dentro da pagina em UserDto
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getSalary()

                ));
    }


    public UserDto findById(Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não existe"));

        return new UserDto(                //Instancia a classe Dto (UserDto) para retornar, os dados corretos, sem a senha...
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getSalary());

    }

    public void deleteUser(Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não existe"));

        userRepository.delete(user);
    }


    public UserDto updateUser(Long id, UpdateUser updateUser){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não existe"));

        if (updateUser.name() != null) user.setName(updateUser.name());  //Se o nome NÃO for nulo, atualize o nome, caso contrário, mantenha o antigo
        if (updateUser.email() != null) user.setEmail(updateUser.email());
        if (updateUser.password() != null) user.setPasswordUser(passwordEncoder.encode(updateUser.password()));
        if (updateUser.salary() != null) user.setSalary(updateUser.salary());

        User saved = userRepository.save(user);

        return new UserDto(saved.getId(), saved.getUsername(),saved.getEmail(), saved.getSalary());
    }

}