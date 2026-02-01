package com.devestudo.projeto_financas.repository;

import com.devestudo.projeto_financas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository  extends JpaRepository<User,Long>
{
    User findByEmail(String email); //buscar usuario por email
}
