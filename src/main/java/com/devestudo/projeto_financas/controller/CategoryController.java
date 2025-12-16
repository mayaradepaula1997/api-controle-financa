package com.devestudo.projeto_financas.controller;

import com.devestudo.projeto_financas.entities.Category;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.CategoryResponseDto;
import com.devestudo.projeto_financas.entities.dtos.CreateCategoryDto;
import com.devestudo.projeto_financas.entities.dtos.UpdateCategoryDto;
import com.devestudo.projeto_financas.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CreateCategoryDto createCategoryDto, Authentication authentication){

        User user = (User) authentication.getPrincipal(); //PESQUISAR

        Category category = categoryService.createCategory(createCategoryDto, user.getId()); //Garante que a categoria seja criada pelo usuario autenticado

        CategoryResponseDto response = new CategoryResponseDto(category.getId(), category.getName(), user.getId(), user.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


    /*@GetMapping
    public ResponseEntity<List<Category>> listAll(Authentication authentication){

        User user = (User) authentication.getPrincipal();

        List<Category> categoryList = categoryService.categoryList(user.getId());

        return ResponseEntity.ok(categoryList);
    }*/



    //Método que retorna a lista que categoria vinculado aquele usuario
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategoriesByUser(@AuthenticationPrincipal User user){ //injeta automaticamento o usuario autenticado

        List<CategoryResponseDto> response = categoryService.getCategoriesByUser(user.getId())//Pega a lista de categorias daquel usuario
                .stream()   //transforma a lista que recebeu
                .map(category -> new CategoryResponseDto( //map: para cada categoria, vou criar a categoria dto (Entity → DTO)
                        category.getId(),
                        category.getName(),
                        user.getId(),
                        user.getName()
                ))
                .toList(); //transforma novamente em uma lista


        return ResponseEntity.ok(response);

    }


    //Buscar uma categoria pelo seu id e garante que a categoria pertence ao usuário logado
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> findById (@PathVariable Long categoryId, @AuthenticationPrincipal User user){

        Category category = categoryService.findById(categoryId, user.getId());

        CategoryResponseDto responseDto = new CategoryResponseDto( //Retorna na categoria no formato DTO
                category.getId(),
                category.getName(),
                user.getId(),
                user.getName()
        );

        return ResponseEntity.ok(responseDto);

    }



    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable Long categoryId, @AuthenticationPrincipal User user, @RequestBody UpdateCategoryDto updateCategoryDto){


        Category update = categoryService.update(categoryId, user.getId(), updateCategoryDto);

       CategoryResponseDto responseDto = new CategoryResponseDto(
               update.getId(),
               update.getName(),
               user.getId(),
               user.getName()

       );

       return ResponseEntity.ok(responseDto);
    }




    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId, @AuthenticationPrincipal User user){


       categoryService.delete(categoryId, user.getId()); //chama o service para excluir

       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
