package com.example.analytics_annotations.annotations

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class AnalyticsAttribute(val attributeName: String, val isNestedObject: Boolean = false)