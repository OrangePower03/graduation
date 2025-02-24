package com.example.backend.domain.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Objects;

import static com.example.backend.constants.Neo4jConstants.SYMPTOM_NODE;

@EqualsAndHashCode(callSuper = true)
@Data
@Node(SYMPTOM_NODE)
@NoArgsConstructor
@AllArgsConstructor
public class SymptomNode extends BaseNode {
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

    @Override
    public String getIdKey() {
        return NAME;
    }

    @Override
    public String getIdValue() {
        return name;
    }
}
