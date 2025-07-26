package com.example.jpaelastic.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "products") // products 인덱스 생성
@Setting(settingPath = "/elasticsearch/product-settings.json")
public class ProductDocument {

    @Id
    private String id;

    // getter, setter 메서드

    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "products_name_analyzer"),
            otherFields = {
                    @InnerField(suffix = "auto_complete", type = FieldType.Search_As_You_Type, analyzer = "nori")
            }
    )
    private String name;

    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "products_category_analyzer"),
            otherFields = {
                    @InnerField(suffix = "raw", type = FieldType.Keyword)
            }
    )
    private String category;
}
