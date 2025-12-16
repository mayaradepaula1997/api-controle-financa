package com.devestudo.projeto_financas.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
//CLASSE GASTO
@Entity
@Table(name ="tb_expense")
public class Expense {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal value;

    private LocalDate localDate;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true) //Deixar categoria como opcional
    private Category category; // opcional

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // dono do gasto

    //CONSTRUTOR VAZIO
    public Expense(){

    }

    //CONSTRUTOR COM ARGUMENTOS
    public Expense(String name, BigDecimal value, LocalDate localDate, String description, Category category, User user) {
        this.name = name;
        this.value = value;
        this.localDate = localDate;
        this.description = description;
        this.category = category;
        this.user = user;
    }

    //CONSTRUTOS SEM A CATEGORIA
    public Expense(Long id, String name, BigDecimal value, LocalDate localDate, String description, User user) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.localDate = localDate;
        this.description = description;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
