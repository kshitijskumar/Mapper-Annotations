package com.example.analytics_processor.processor.model

data class AnalyticsParamData(
    val packageName: String,
    val modelName: String,
    val parentName: String,
    val attributesData: List<AnalyticsAttributeData>
)
