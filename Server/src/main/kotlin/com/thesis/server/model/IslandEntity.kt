package com.thesis.server.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Property

@Node("Island")
class IslandEntity {
    @Id
    private val name: String? = null

    @Property
    private val address: String? = null
}