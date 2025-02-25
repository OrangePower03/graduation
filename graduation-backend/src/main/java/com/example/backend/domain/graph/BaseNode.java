package com.example.backend.domain.graph;

import com.example.backend.utils.AssertUtils;
import org.springframework.data.neo4j.core.schema.Node;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public abstract class BaseNode implements Serializable {
    @Serial
    private static final long serialVersionUID = 114514L;

    public String getNodeName() {
        Node node = getClass().getAnnotation(Node.class);
        AssertUtils.nonNull(node, "Node annotation not found");
        String[] value = node.value();
        return value[0];
    }

    abstract public String getIdKey();

    abstract public String getIdValue();

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseNode that = (BaseNode) o;
        return Objects.equals(getIdValue(), that.getIdValue());
    }
}
