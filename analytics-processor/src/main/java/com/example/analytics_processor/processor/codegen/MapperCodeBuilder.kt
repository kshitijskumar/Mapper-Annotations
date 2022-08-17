package com.example.analytics_processor.processor.codegen

import com.example.analytics_processor.processor.model.AnalyticsParamData
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName

class MapperCodeBuilder(
    private val mapperName: String,
    private val data: AnalyticsParamData
) {

    private val modelClassName = ClassName(data.packageName, data.modelName)
    private val extensionFunctionName = "toAnalyticsParamsMap"

    fun build(): FunSpec {
        val type = Map::class.asClassName()
            .parameterizedBy(
                String::class.asClassName(),
                String::class.asClassName()
            )

        mutableMapOf<String, String>().mapKeys {
            it.key
        }

        return FunSpec.builder(extensionFunctionName)
            .receiver(modelClassName)
            .returns(type)
            .addStatement("val paramMap = mutableMapOf<String, String>()")
            .apply {
                data.attributesData.forEach {
                    if (!it.isNestedObject) {
                        addStatement("paramMap[%S] = this.%L.toString()", it.attributeName, it.attributeValue)
                    } else {
                        addStatement("""
                            val nestedObjectParams = this.%L.toAnalyticsParamsMap().mapKeys { %S + "_" + it.key } 
                            println(nestedObjectParams)
                            paramMap.putAll(nestedObjectParams)
                            """.trimIndent(), it.attributeValue, it.attributeName)
                    }
                }
            }
            .addStatement("return paramMap")
            .build()
    }

}