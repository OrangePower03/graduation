package com.example.backend.domain.graph;

import com.example.backend.utils.AssertUtils;
import org.springframework.data.neo4j.core.schema.Node;
import java.io.Serializable;

public abstract class BaseNode implements Serializable {
    private static final long serialVersionUID = 114514L;

    public String getNodeName() {
        Node node = getClass().getAnnotation(Node.class);
        AssertUtils.nonNull(node, "Node annotation not found");
        String[] value = node.value();
        return value[0];
    }

    abstract public String getIdKey();

    abstract public String getIdValue();
}
