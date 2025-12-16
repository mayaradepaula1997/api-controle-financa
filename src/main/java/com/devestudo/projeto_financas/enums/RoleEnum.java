package com.devestudo.projeto_financas.enums;

public enum RoleEnum {

    ADMIN("admin"),
    USER("user");

    private final String role;

    //Construtor
    RoleEnum(String role) {
        this.role = role;
    }

    //Getter
    public String getRole() {
        return role;
    }
}


