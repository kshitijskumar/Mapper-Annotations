package com.example.analytics_processor.processor.model

data class AnalyticsAttributeData(
    val attributeName: String,
    val attributeValue: String,
    val isNestedObject: Boolean
)
