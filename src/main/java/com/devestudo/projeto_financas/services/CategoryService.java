package com.devestudo.projeto_financas.services;

import com.devestudo.projeto_financas.entities.Category;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.request.CategoryAvailableDto;
import com.devestudo.projeto_financas.entities.dtos.request.CreateCategoryDto;
import com.devestudo.projeto_financas.entities.dtos.request.UpdateCategoryDto;
import com.devestudo.projeto_financas.enums.CategoryType;
import com.devestudo.projeto_financas.exception.BusinessException;
import com.devestudo.projeto_financas.exception.ResourceNotFoundException;
import com.devestudo.projeto_financas.filter.CategorySpecification;
import com.devestudo.projeto_financas.repository.CategoryRepository;
import com.devestudo.projeto_financas.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    //injeção de dependencia atraves do construtor
    private CategoryRepository categoryRepository;

    private UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    //Criação
    public Category createCategory(CreateCategoryDto createCategoryDto, Long userId){

       User user = userRepository.findById(userId)
               .orElseThrow(()-> new ResourceNotFoundException("Usário não encontrado"));

        Optional<Category> optionalCategory = categoryRepository.findByNameAndUser(createCategoryDto.name(),user);

      if(optionalCategory.isPresent())   //se a categiria já exister, lanço a exceção
          throw new BusinessException("Categoria já existe!");

      //Se a categoria não existir, vamos fazer a instanciação
      Category category = new Category(); //CategoryType.USER/ enum: quando for criar uma categoria, sempre vai ser USER
        category.setName(createCategoryDto.name());
        category.setCategoryType(CategoryType.USER);
        category.setUser(user);


        return categoryRepository.save(category); //salva a categoria no banco de dados

    }


    //Método que vai retornar todas as categorias (SISTEMA E USUÁRIO) - Categorias disponiveis(Se for SISTEMA e se pertencer ao USUÁRIO logado
    //Long userId - id do usuárioque está fazendo a requisição
    public Page<CategoryAvailableDto> findAvailableCategories(Long userId, int page, int size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        //Busca todas as categorias que o usuário pode ver, passando o filtro
        Page<Category> categories = categoryRepository.findAll(
                CategorySpecification.availableForUser(userId),pageable);

        //Mapea cada categoria que vem do BD, retornando o DTO
        return categories.map(category -> new CategoryAvailableDto(
                category.getId(),
                category.getName(),
                category.getCategoryType()
        ));
    }


    //Método para listar as categorias criadas pelo usuário - EM FORMATO DE PAGINAÇÃO
    public Page<Category> getCategoriesByUser(int page, int size, Long userId){

        Optional<User> optionalUser = userRepository.findById(userId); //buscamos o usuario no BD

            if(optionalUser.isEmpty()){
                throw new ResourceNotFoundException("Usuário não encontrado"); //se não existir lança a exceção

            }

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending()); //ordenando pelo nome em ordem decrecente

            return categoryRepository.findByUserId(userId, pageable);
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


    //Método para garantir que apenas o usuário dono da categoria possa deletá-la e se NÃO for do tipo SYSTEM
    public void delete(Long categoryId, Long userId){

       Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria Não encontrada")); //busca a categoria pelo id

        if (category.getCategoryType() == CategoryType.SYSTEM){
            throw new ResourceNotFoundException("Categorias do sistema não podem ser remotivas");
        }

        //Verifica se a categoria pertence ao usuário
       if(!category.getUser().getId().equals(userId)){ //acessa e retorna o usuario associado aquela categoria, em seguida pego o id do usuario e comprara com o id
                                                        // ! negação: Se o ID do dono da categoria não for igual ao ID do usuário que está tentando deletar..

           throw new BusinessException("Você não tem permissão para deletar esta categoria");
       }

        categoryRepository.delete(category); // chama o método delete do repository
    }





    //Método para atualizar a categoria apenas o usuário dono da categoria e se NÃO for do tipo SYSTEM
    public Category update(Long categoryId, Long userId, UpdateCategoryDto updateCategoryDto){

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não existe")); //verifico se a categoria existe pelo id

        if (category.getCategoryType() == CategoryType.SYSTEM)
            throw new ResourceNotFoundException("Você não pode atualizar essa categoria ");

        // Verifica se a categoria pertence ao usuário
        if(!category.getUser().getId().equals(userId)){ //Compara o userId recebido com o userId da categoria. Se forem diferentes, lança uma exceção

            throw new BusinessException("Você não tem permissão para atualizar esta categoria");
        }

        category.setName(updateCategoryDto.name());  //Atualiza o nome da categoria (se existir)

        return categoryRepository.save(category);
    }

}
