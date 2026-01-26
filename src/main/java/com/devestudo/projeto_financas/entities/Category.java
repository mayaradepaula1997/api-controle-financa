package com.devestudo.projeto_financas.entities;

import com.devestudo.projeto_financas.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @Enumerated(EnumType.STRING) //ENUM que será persistido no BD, será salvo como texto e não como numero
    private CategoryType categoryType;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = true) //FK que liga à tabela de usuários
    @JsonIgnoreProperties("categories")
    private User user;

    //Construtor Vazio
    public Category(){

    }

    //Construtor com argumentos, sem passar o id
    public Category(String name, CategoryType categoryType, User user) {
        this.name = name;
        this.categoryType = categoryType;
        this.user = user;
    }

    //Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //Método de comparação atraves do id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
