package com.example.backend.domain.graph.relationship;

import com.example.backend.domain.graph.FoodNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class FoodCategory_Food {
    public final static String ID = "id";

    @Id
    @Property(ID)
    @GeneratedValue
    private Long id;

    @TargetNode
    private FoodNode foodNode;
}
