package com.example.analytics_annotations.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AnalyticsParam(val parentName: String)