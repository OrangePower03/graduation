package com.example.backend.constants;

public interface Neo4jConstants {
    String INDICATOR_NODE = "Indicator";

    String SYMPTOM_NODE = "Symptom";

    String FOOD_CATEGORY_NODE = "FoodCategory";

    String FOOD_NODE = "Food";

    String INDICATOR_SYMPTOM_LOW_RELATION = "Indicator_Symptom_LOW";

    String INDICATOR_SYMPTOM_HIGH_RELATION = "Indicator_Symptom_HIGH";

    String SYMPTOM_FOOD_CATEGORY_RECOMMEND_RELATION = "Symptom_FoodCategory_RECOMMEND";

    String SYMPTOM_FOOD_CATEGORY_UNRECOMMEND_RELATION = "Symptom_FoodCategory_UNRECOMMEND";

    String FOOD_CATEGORY_FOOD_RELATION = "FoodCategory_Food";
}
