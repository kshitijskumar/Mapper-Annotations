package com.example.analytics_processor.processor

import com.example.analytics_annotations.annotations.AnalyticsAttribute
import com.example.analytics_annotations.annotations.AnalyticsParam
import com.example.analytics_processor.processor.codegen.MapperCodeBuilder
import com.example.analytics_processor.processor.model.AnalyticsAttributeData
import com.example.analytics_processor.processor.model.AnalyticsParamData
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class Processor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(AnalyticsParam::class.java.canonicalName)
    }

    override fun process(p0: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "processor started")
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: return false
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "processor kapt: $kaptKotlinGeneratedDir")

        roundEnv.getElementsAnnotatedWith(AnalyticsParam::class.java)
            .forEach {
                processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "ap ele: $it")
                val paramData = getAnalyticsParamData(it)

                val fileName = "${paramData.modelName}AnalyticsMapper"
                FileSpec.builder(paramData.packageName, fileName)
                    .addFunction(MapperCodeBuilder(fileName, paramData).build())
                    .build()
                    .writeTo(File(kaptKotlinGeneratedDir))

            }

        return true
    }

    private fun getAnalyticsParamData(element: Element): AnalyticsParamData {
        val packageName = processingEnv.elementUtils.getPackageOf(element).toString()
        val modelName = element.simpleName.toString()
        val annotation = element.getAnnotation(AnalyticsParam::class.java)
        val parentName = annotation.parentName

        val attributesData = getListOfAnalyticsAttributes(element)
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "attributes data: $attributesData")


        return AnalyticsParamData(
            packageName = packageName,
            modelName = modelName,
            parentName = parentName,
            attributesData = attributesData
        )
    }

    private fun getListOfAnalyticsAttributes(element: Element): List<AnalyticsAttributeData> {
        val attributesData: MutableList<AnalyticsAttributeData> = mutableListOf()
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "for: $element - enclosed: ${element.enclosedElements}")
        element.enclosedElements.forEach {
            processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "each: $it")
            val analyticsAttribute = it.getAnnotation(AnalyticsAttribute::class.java)
            if (analyticsAttribute != null) {
                val elementName = it.simpleName.toString()
                val fieldName = if (elementName.startsWith("get")) {
                    val getRemovedElementName = elementName.substring(3, elementName.indexOf("$"))
                    val updatedElementName = getRemovedElementName
                        .replace(getRemovedElementName[0], getRemovedElementName[0].lowercaseChar())
                    updatedElementName
                } else {
                    elementName.substring(0, elementName.indexOf("$"))
                }
                processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "else element: $elementName")
                val attributeName = analyticsAttribute.attributeName
                val isNestedObject = analyticsAttribute.isNestedObject

                it.asType().kind.isPrimitive

                val attrData = AnalyticsAttributeData(
                    attributeName = attributeName,
                    attributeValue = fieldName,
                    isNestedObject = isNestedObject
                )

                attributesData.add(attrData)
            }
        }

        return attributesData
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"


    }
}