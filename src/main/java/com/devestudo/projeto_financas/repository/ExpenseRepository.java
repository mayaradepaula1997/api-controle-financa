package com.devestudo.projeto_financas.repository;

import com.devestudo.projeto_financas.entities.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUserId(Long userId, Pageable pageable);//método que busca todas as categorias que um usuario em formato de paginação
}
