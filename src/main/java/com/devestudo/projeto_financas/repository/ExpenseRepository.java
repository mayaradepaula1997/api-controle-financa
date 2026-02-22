package com.devestudo.projeto_financas.repository;

import com.devestudo.projeto_financas.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

//Já possui o CRUD e a paginação(Pageable)
//JpaSpecificationExecutor: Permite utilizar o Specification, que cria filtros dinâmicos usando criterios

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    //Busca os gastos do usuário
    List<Expense> findByUserId(Long userId);
}
