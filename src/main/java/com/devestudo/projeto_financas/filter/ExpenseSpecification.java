package com.devestudo.projeto_financas.filter;
import com.devestudo.projeto_financas.entities.Expense;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.LocalDate;

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


    //Filtro pelo valor maximo, se for NULL, o filtro não é aplicado
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

    //Busca os gastos de tenha no nome ou na descrição a String que foi passada. Ex: "Uber"
    public static Specification<Expense> nameOrDescriptionContains(String text){

        return (root, query, cb) ->{

           if (text == null || text.isBlank()){
               return cb.conjunction();  //Não vai filtrar nada, vai trazer todos os gastos
           }

           String like = "%" + text.toLowerCase() + "%";  //Permite buscar a palavra e igora maiúsculas/minúsculas

            Predicate namePredicate = cb.like(cb.lower(root.get("name")),like); //Condição WHERE, se for verdadeira irá retornar

            Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), like);

            return cb.or(namePredicate, descriptionPredicate); //Vai trazer o gasto se o nome bater OU a descrição bater

        };
    }

    /*Método que vai retorna os gastos por periodo (dataInicio - dataFinal) se não for passado o perido
    vai retornar os gasto daquele mês*/
    public static Specification<Expense> byPeriod (LocalDate dateStart,LocalDate endDate) {

        return (root, query, cb) ->
            cb.between(root.get("localDate"), dateStart, endDate);
        }

    }


