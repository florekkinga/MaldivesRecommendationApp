package com.thesis.server.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node("Rating")
class RatingEntity {
    @Id
    private val rating: String? = null

    @Relationship(type = "RATING", direction = Relationship.Direction.OUTGOING)
    private val islands: Set<IslandEntity> = HashSet<IslandEntity>()
}