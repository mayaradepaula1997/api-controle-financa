package com.devestudo.projeto_financas.filter;
import com.devestudo.projeto_financas.entities.Expense;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

//Usada no Spring Data JPA para montar filtros dinâmicos de busca no banco de dados, usando o padrão Specification.

public class ExpenseSpecification {

    //Retorna os gastos do usuário logado
    public static Specification<Expense> byUser(Long userId) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("id"), userId);
    }

    //Filtro pelo valor minino, se for NULL, o filtro não é aplicado
    public static Specification<Expense> minValue(BigDecimal minValue) {
        return (root, query, cb) ->
                minValue == null ? null :
                        cb.greaterThanOrEqualTo(root.get("value"), minValue);
    }


    //Filtro pelo valor maximi, se for NULL, o filtro não é aplicado
    public static Specification<Expense> maxValue(BigDecimal maxValue) {
        return (root, query, cb) ->
                maxValue == null ? null :
                        cb.lessThanOrEqualTo(root.get("value"), maxValue);
    }


    //Filtra gastos por categoria específica. Se não passar categoria, retorna todas
    public static Specification<Expense> category(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null :
                        cb.equal(root.get("category").get("id"), categoryId);
    }

}
