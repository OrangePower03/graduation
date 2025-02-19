package com.example.backend.domain.graph.relationship;

import com.example.backend.domain.graph.FoodCategoryNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class Symptom_FoodCategory {
    public final static String ID = "id";
    public final static String SUGGESTION = "suggestion";

    public final static String RECOMMEND = "recommend";
    public final static String UNRECOMMEND = "unrecommend";

    @Id
    @Property(ID)
    @GeneratedValue
    private Long id;

    @Property(SUGGESTION)
    private String suggestion;

    @TargetNode
    private FoodCategoryNode foodCategoryNode;
}
