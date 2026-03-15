package com.devestudo.projeto_financas.initializer;

import com.devestudo.projeto_financas.entities.Category;
import com.devestudo.projeto_financas.enums.CategoryType;
import com.devestudo.projeto_financas.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//CLASSE QUE INICIALIZA OS DADOS QUANDO A APLICAÇÃO SUBIR
@Component  //Quando o projeto rodar vai procurar por essa anotação, que é gerenciada pelo Spring
public class CategoryInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    //Injeção de dependência
    public CategoryInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //Método que irá verificar se a categoria já existe ou não no BD
    private void createIfNotExists(String name){

        if (!categoryRepository.existsByNameAndCategoryType(name, CategoryType.SYSTEM)){ //se esse "nome" e "categoria" não existir no BD

            Category category = new Category(); //Se não existir no BD, crio obj Category
            category.setName(name);
            category.setCategoryType(CategoryType.SYSTEM);

            categoryRepository.save(category); //Salva o obj no BD
        }
    }

    //Esse método é executado uma unica vez, na inicialização da aplicação
    @Override
    public void run(String... args) throws Exception {
        createIfNotExists("Alimentação");
        createIfNotExists("Transporte");
        createIfNotExists("Moradia");
        createIfNotExists("Lazer");
        createIfNotExists("Educação");
        createIfNotExists("Outros");
    }
}
