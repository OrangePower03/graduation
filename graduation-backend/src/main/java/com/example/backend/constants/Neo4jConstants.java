package com.example.backend.constants;

public interface Neo4jConstants {
    String INDICATOR_NODE = "Indicator";

    String SYMPTOM_NODE = "Symptom";

    String FOOD_CATEGORY_NODE = "FoodCategory";

    String FOOD_NODE = "Food";

    String INDICATOR_SYMPTOM_RELATION = "Indicator_Symptom";

    String SYMPTOM_FOOD_CATEGORY_RELATION = "Symptom_FoodCategory";

    String FOOD_CATEGORY_FOOD_RELATION = "FoodCategory_Food";
}
