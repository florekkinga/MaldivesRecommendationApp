package com.thesis.server.service

import org.springframework.stereotype.Service

@Service
class RecommendationService {
    fun getRecommendation(features: String): String {
        return "recommendation for $features"
    }
}