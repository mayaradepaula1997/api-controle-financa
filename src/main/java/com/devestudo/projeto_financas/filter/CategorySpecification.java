package com.devestudo.projeto_financas.filter;

import com.devestudo.projeto_financas.entities.Category;
import com.devestudo.projeto_financas.enums.CategoryType;
import org.springframework.data.jpa.domain.Specification;


//Classe que monta filtros dinâmicos
//Busca todas as categoria do usuario (SYSTEM OU USER)
public class CategorySpecification {

    public static Specification<Category> availableForUser(Long userId){  //Sinaliza que esse filtro vai ser aplicado na classe caterogia/where

        return ((root, query, cb) ->

              cb.or(   //Quero que categoria atenda uma dessas regras
              cb.equal(root.get("categoryType"), CategoryType.SYSTEM),         //Se o campo "categoryType" da entida Category for igual a SYSTEM
              cb.and(
                      cb.equal(root.get("categoryType"), CategoryType.USER),   //Se a campo "categoryType" da entidade Category for igual a USER
                      cb.equal(root.get("user").get("id"),userId)              //cd.adn - só retorna categoria criadas pelo usuário

                )
              )
        );
    }
}



/*
(type = SYSTEM)
OR
(type = USER AND user.id = userId)
 */