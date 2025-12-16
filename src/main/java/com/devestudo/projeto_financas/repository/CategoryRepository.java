package com.devestudo.projeto_financas.repository;

import com.devestudo.projeto_financas.entities.Category;
import com.devestudo.projeto_financas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository  extends JpaRepository<Category, Long> {


    Optional<Category> findByName(String name); //método para buscar categoria pelo nome

    List<Category> findByUserId(Long userId); //método que busca todas as categorias de um usuario

    Optional<Category> findByNameAndUser(String name, User user);  //método que busca a categoria pelo nome e que pertença ao usuario informado


}
