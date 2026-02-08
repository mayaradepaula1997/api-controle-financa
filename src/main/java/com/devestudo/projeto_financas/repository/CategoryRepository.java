package com.devestudo.projeto_financas.repository;

import com.devestudo.projeto_financas.entities.Category;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

//JpaSpecificationExecutor: Utilizado quanto precisamos realizar um filtro dinâmico
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {


    //Retorna em formato de paginação
    Page<Category> findByUserId(Long userId, Pageable pageable); //método que busca todas as categorias de um usuario

    Optional<Category> findByNameAndUser(String name, User user);  //método que busca a categoria pelo nome e que pertença ao usuario informado

    boolean existsByNameAndCategoryType(String name, CategoryType type);  //método que busca a categoria pelo nome e pelo seu tipo(ENUM, SYSTEM)
}
