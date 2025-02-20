package com.example.backend.service;

import com.example.backend.domain.graph.BaseNode;
import org.neo4j.cypherdsl.core.Node;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.neo4j.cypherdsl.core.Cypher.*;

@Service
public class Neo4jService {
    @Autowired
    private Driver neo4jDriver;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Transactional(rollbackFor = Exception.class, transactionManager = "neo4jTransactionManager")
    public int addNodes(Set<? extends BaseNode> nodes) {
        return neo4jTemplate.saveAll(nodes).size();
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "neo4jTransactionManager")
    public int addRelationsById(BaseNode parentNode, Set<? extends BaseNode> childrenNodes, String relationName) {

        Node parent = node(parentNode.getNodeName()).named("parent").withProperties(Map.of(parentNode.getIdKey(), parentNode.getIdValue()));
        List<String> cql = childrenNodes.stream().map(childrenNode -> {
            Node child = node(childrenNode.getNodeName()).named("child").withProperties(Map.of(childrenNode.getIdKey(), childrenNode.getIdValue()));
            return match(parent).match(child)
                    .create(parent.relationshipTo(child, relationName))
                    .build().getCypher();
        }).collect(Collectors.toList());
        return executeCql(cql);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "neo4jTransactionManager")
    public int executeCql(List<String> cql) {
        try(Session session = neo4jDriver.session()) {
            for (String c : cql) {
                session.run(c);
            }
            return cql.size();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "neo4jTransactionManager")
    public void deleteAll() {
        String cql = "MATCH (n) DETACH DELETE n";
        executeCql(List.of(cql));
    }
}
