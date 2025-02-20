package com.example.backend.domain.graph;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Objects;

import static com.example.backend.constants.Neo4jConstants.FOOD_NODE;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodNode that = (FoodNode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String getIdKey() {
        return NAME;
    }

    @Override
    public String getIdValue() {
        return name;
    }
}
