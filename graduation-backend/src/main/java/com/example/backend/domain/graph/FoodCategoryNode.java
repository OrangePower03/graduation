package com.example.backend.domain.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import static com.example.backend.constants.Neo4jConstants.FOOD_CATEGORY_NODE;

@Data
@Node(FOOD_CATEGORY_NODE)
@NoArgsConstructor
@AllArgsConstructor
public class FoodCategoryNode {
    public final static String NAME = "name";

    @Id
    @Property(NAME)
    private String name;
}
