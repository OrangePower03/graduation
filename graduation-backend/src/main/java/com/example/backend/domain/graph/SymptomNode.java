package com.example.backend.domain.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import static com.example.backend.constants.Neo4jConstants.SYMPTOM_NODE;

@Data
@Node(SYMPTOM_NODE)
@NoArgsConstructor
@AllArgsConstructor
public class SymptomNode {
    public final static String NAME = "name";
    public final static String INDUCED = "induced";
    public final static String SUGGESTION = "suggestion";

    @Id
    @Property(NAME)
    private String name;

    @Property(INDUCED)
    private String induced;

    @Property(SUGGESTION)
    private String suggestion;
}
