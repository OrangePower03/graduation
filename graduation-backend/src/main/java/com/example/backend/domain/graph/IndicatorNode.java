package com.example.backend.domain.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Objects;

import static com.example.backend.constants.Neo4jConstants.INDICATOR_NODE;

@Data
@Node(INDICATOR_NODE)
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorNode extends BaseNode {
    public final static String ID = "id";
    public final static String NAME = "name";

    @Id
    @Property(ID)
    private String id;

    @Property(NAME)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndicatorNode that = (IndicatorNode) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String getIdKey() {
        return ID;
    }

    @Override
    public String getIdValue() {
        return id;
    }
}
