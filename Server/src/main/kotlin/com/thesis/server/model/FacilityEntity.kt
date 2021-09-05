package com.thesis.server.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node("Facility")
class FacilityEntity {
    @Id
    private val name: String? = null

    @Relationship(type = "FACILITY", direction = Relationship.Direction.OUTGOING)
    private val islands: Set<IslandEntity> = HashSet<IslandEntity>()
}