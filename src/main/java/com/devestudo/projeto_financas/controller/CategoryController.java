package com.devestudo.projeto_financas.controller;

import com.devestudo.projeto_financas.entities.Category;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.CategoryAvailableDto;
import com.devestudo.projeto_financas.entities.dtos.CategoryResponseDto;
import com.devestudo.projeto_financas.entities.dtos.CreateCategoryDto;
import com.devestudo.projeto_financas.entities.dtos.UpdateCategoryDto;
import com.devestudo.projeto_financas.services.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


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

    //Método que retorna todas as categoria (SYSTEM E USER) que o usuário pode visualizar
    @GetMapping("/available")
    public ResponseEntity<Page<CategoryAvailableDto>> getAvailableCategories(
            @AuthenticationPrincipal User user,  //Usuário autenticado
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue ="5") int size
    ){

        Long userId = user.getId();   //Usa o id do usuário autenticado

        Page<CategoryAvailableDto> categories = categoryService.findAvailableCategories(userId, page, size);

        return ResponseEntity.ok(categories);
    }



    //OBS:  nunca usar STREAM() quando estiver trabalhando com Page
    //O Page do Spring tem seu proprio page.map(), transforma Entity - DTO
    //Busca as categorias cadastradas para o usuário autenticado - PAGINAÇÃO
    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> getCategoriesByUser(
            @AuthenticationPrincipal User user,                      //injeta automaticamento o usuario autenticado
            @RequestParam(defaultValue = "0") int page,              //se a paginanão não for passada, por padrão vai vim a primeira pagina
            @RequestParam(defaultValue = "5") int size
    ){

        Page<CategoryResponseDto> response = categoryService.getCategoriesByUser(page, size,user.getId())       //Pega a lista de categorias daquele usuario
                                                                                                               //user.geyId -> pega o id do usuário autentidado

                .map(category -> new CategoryResponseDto(                     //Map -> Pega cada categoria e transforma CategoryResponseDto
                        category.getId(),
                        category.getName(),
                        user.getId(),
                        user.getName()
                ));

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
