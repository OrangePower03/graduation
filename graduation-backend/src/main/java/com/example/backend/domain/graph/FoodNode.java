package com.example.backend.domain.graph;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Objects;

import static com.example.backend.constants.Neo4jConstants.FOOD_NODE;

@EqualsAndHashCode(callSuper = true)
@Data
@Node(FOOD_NODE)
@NoArgsConstructor
@AllArgsConstructor
public class FoodNode extends BaseNode {
    public final static String NAME = "name";

    @Id
    @Property(NAME)
    private String name;

    @Override
    public String getIdKey() {
        return NAME;
    }

    @Override
    public String getIdValue() {
        return name;
    }
}
