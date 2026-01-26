package com.devestudo.projeto_financas.enums;

//REPRESENTA UM CONJUNTO DE VALORES FIXOS
public enum CategoryType {

    SYSTEM("system"),
    USER("user");


    //Atributo - Sua descrição
    private final String categoryType;

    //Construtor
    CategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    //Getter
    public String getCategoryType() {
        return categoryType;
    }
}
