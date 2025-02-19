package com.example.backend.domain.graph.relationship;

import com.example.backend.domain.graph.SymptomNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class Indicator_Symptom {
    public final static String ID = "id";
    public final static String INDICATOR_VALUE = "indicator_value";

    public final static String INDICATOR_VALUE_LOW = "LOW";
    public final static String INDICATOR_VALUE_HIGH = "HIGH";

    @Id
    @Property(ID)
    @GeneratedValue
    private Long id;

    @Property(INDICATOR_VALUE)
    private String indicatorValue;

    @TargetNode
    private SymptomNode symptomNode;
}
