package com.thesis.server.controller

import com.thesis.server.service.RecommendationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class RecommendationController(private val recommendationService: RecommendationService) {

    @GetMapping("/recommendation/{features}")
    fun getRecommendation(@PathVariable("features") features: String): String {
        return recommendationService.getRecommendation(features)
    }
}