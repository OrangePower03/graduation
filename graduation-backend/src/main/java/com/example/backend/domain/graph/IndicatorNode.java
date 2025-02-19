package com.example.backend.domain.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import static com.example.backend.constants.Neo4jConstants.INDICATOR_NODE;

@Data
@Node(INDICATOR_NODE)
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorNode {
    public final static String ID = "id";
    public final static String NAME = "name";

    @Id
    @Property(ID)
    private Long id;

    @Property(NAME)
    private String name;
}
