package com.devestudo.projeto_financas.services;

import com.devestudo.projeto_financas.entities.Category;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.CreateCategoryDto;
import com.devestudo.projeto_financas.entities.dtos.UpdateCategoryDto;
import com.devestudo.projeto_financas.exception.BusinessException;
import com.devestudo.projeto_financas.exception.ResourceNotFoundException;
import com.devestudo.projeto_financas.repository.CategoryRepository;
import com.devestudo.projeto_financas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Após concluir todo o fluxo de categoria, tenta criar uma função reutilizavel para buscar a categoria daquele determinado usuaria (função utilitaria)
//Função utilizataria (criar ela no pacote util) - Função que se repete ao longo do sistema,

@Service
public class CategoryService {

    //injeção de dependencia atraves do construtor
    private CategoryRepository categoryRepository;

    private UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Category createCategory(CreateCategoryDto createCategoryDto, Long userId){

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty())
            throw new ResourceNotFoundException("Usuário não encontrado");

        Optional<Category> optionalCategory = categoryRepository.findByNameAndUser(createCategoryDto.name(), optionalUser.get());

      if(optionalCategory.isPresent())   //se a categiria já exister, lanço a exceção
          throw new BusinessException("Categoria já existe!");

      Category category = new Category(createCategoryDto.name(), optionalUser.get()); //se a categoria não existir, vamos instanciar a classe e passar os atributos

        return categoryRepository.save(category); //salva a categoria no banco de dados
    }



   /* //Método para listar categoria
    public List<Category> categoryList(Long userId){  // PESQUISAR

        return categoryRepository.findByUserId(userId);

    }*/



    //Método para listar as categorias daquele determinado usuario
    public List<Category> getCategoriesByUser(Long userId){

        Optional<User> optionalUser = userRepository.findById(userId); //buscamos o usuario no BD

            if(optionalUser.isEmpty()){
                throw new ResourceNotFoundException("Usuário não encontrado"); //se não existir lança a exceção

            }
            return categoryRepository.findByUserId(userId); //se ele existir, retornamos a lista de categorias vinculadas
    }





    // Método para buscar a categoria pelo id, garantindo que pertence ao usuário
    public Category findById(Long categoryId, Long userId){

      Category category = categoryRepository.findById(categoryId) //buscar a categoria no BD pelo id
                .orElseThrow(()-> new ResourceNotFoundException("Categoria não existe"));

      // Verifica se a categoria pertence ao usuário
      if(!category.getUser().getId().equals(userId)){
          throw new BusinessException("Você não tem permissão para visualizar esta categoria");
      }
      return category;

    }



    //Método para garantir que apenas o usuário dono da categoria possa deletá-la
    public void delete(Long categoryId, Long userId){

       Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria Não encontrada")); //busca a categoria pelo id

        //Verifica se a categoria pertence ao usuário
       if(!category.getUser().getId().equals(userId)){ //acessa e retorna o usuario associado aquela categoria, em seguida pego o id do usuario e comprara com o id
                                                        // ! negação: Se o ID do dono da categoria não for igual ao ID do usuário que está tentando deletar..

           throw new BusinessException("Você não tem permissão para deletar esta categoria");
       }

        categoryRepository.delete(category); // chama o método delete do repository
    }





    //Método para atualizar a categoria apenas o usuário dono da categoria possa atualizar
    public Category update(Long categoryId, Long userId, UpdateCategoryDto updateCategoryDto){

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não existe")); //verifico se a categoria existe pelo id

        // Verifica se a categoria pertence ao usuário
        if(!category.getUser().getId().equals(userId)){ //Compara o userId recebido com o userId da categoria. Se forem diferentes, lança uma exceção

            throw new BusinessException("Você não tem permissão para atualizar esta categoria");
        }

        category.setName(updateCategoryDto.name());  //Atualiza o nome da categoria (se existir)

        return categoryRepository.save(category);
    }

}
