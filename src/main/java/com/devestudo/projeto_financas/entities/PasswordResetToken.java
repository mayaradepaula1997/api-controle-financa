package com.devestudo.projeto_financas.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;     //nullable = false: o campo não pode ser vazio - token enviado ao usuário, campo unico

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expirationDate;   //prazo de validade do token

    @Column(nullable = false)
    private boolean used;      //campo de controle para saber se o token está vazio

    //Construtor vazio
    public PasswordResetToken(){

    }


    public PasswordResetToken(String token, User user, LocalDateTime expirationDate, boolean used) {
        this.token = token;
        this.user = user;
        this.expirationDate = expirationDate;
        this.used = used;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
