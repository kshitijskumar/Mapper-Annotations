package com.example.understandingannotation.demo

import com.example.analytics_annotations.annotations.AnalyticsAttribute
import com.example.analytics_annotations.annotations.AnalyticsParam

@AnalyticsParam("person")
data class SomeState(
    @AnalyticsAttribute("first_name") val firstName: String,
    val secondName: String,
    @AnalyticsAttribute("location_stuff", isNestedObject = true) val location: SomeInnerState,
    @AnalyticsAttribute("rank_in_game") val someNumber: Int = 5
)

@AnalyticsParam("inner")
data class SomeInnerState(
    @AnalyticsAttribute("current_city") val city: String,
    @AnalyticsAttribute("country") val country: String,
    val pin: Int,
    @AnalyticsAttribute("one_more", true) val oneMore: OneMore = OneMore()
)

@AnalyticsParam("random")
data class OneMore(
    @AnalyticsAttribute("random") val random: Int = 32
)

@AnalyticsParam("yo")
data class RandomStuff(
    val a: String
)

fun something() {
    OneMore().toAnalyticsParamsMap()
    RandomStuff("SDfs").toAnalyticsParamsMap()
}