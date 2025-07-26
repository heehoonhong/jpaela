package com.example.jpaelastic.domain;

import lombok.Getter;

@Getter
public class CreateProductRequestDto {
    private String name;
    private String category;

    public CreateProductRequestDto(String name,String category){
        this.category=category;
        this.name=name;
    }
}
