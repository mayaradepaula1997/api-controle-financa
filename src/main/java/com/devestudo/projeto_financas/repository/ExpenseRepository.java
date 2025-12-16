package com.devestudo.projeto_financas.repository;

import com.devestudo.projeto_financas.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);//método que busca todas as categorias que um usuario
}
