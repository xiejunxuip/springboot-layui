package com.haiyu.manager.common.utils

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

private val mapping = jacksonObjectMapper()

fun <R : Any> R.json(): String {
    return mapping.writeValueAsString(this)
}

fun <R : Any> R.log(): String {
    return this.objectToJson(true)
}

fun <R : Any> R.objectToJson(ignoreError: Boolean? = false): String {
    val result = kotlin.runCatching { mapping.writeValueAsString(this) }
    return when(ignoreError) {
        false -> result.getOrThrow()
        else -> result.getOrElse { logger.warn("Json processing error."); "" }
    }
}

fun <T : Any, R : Any> R.mapTo(type: Class<T>): T {
    return mapping.readValue(mapping.writeValueAsString(this), type)
}

fun <T : Any> String.mapTo(type: Class<T>): T {
    return mapping.readValue(this, type)
}

fun <T : Any, R : Any> R.objectToObject(clazz: Class<T>): T {
    return mapping.readValue(mapping.writeValueAsString(this), clazz)
}

fun <T : Any, R : Any> R.objectToObject(javaType: JavaType): T {
    return mapping.readValue(mapping.writeValueAsString(this), javaType)
}

fun <T : Any> String.jsonToObject(clazz: Class<T>): T {
    return mapping.readValue(this, clazz)
}

fun <T : Any> String.jsonToObject(javaType: JavaType): T {
    return mapping.readValue(this, javaType)
}

fun <T : Any> String.jsonToList(clazz: Class<T>): List<*> {
    return mapping.readValue(this, List::class.java.constructParametricType(clazz))
}

fun String.jsonToList(javaType: JavaType): List<*> {
    return mapping.readValue(this, List::class.java.constructParametricType(javaType))
}

fun <K : Any, V : Any> String.jsonToMap(key: Class<K>, value: Class<V>): Map<K, V> {
    return mapping.readValue(this, Map::class.java.constructParametricType(key, value))
}

fun <T : Any> Class<T>.constructParametricType(vararg parameterClasses: Class<*>): JavaType {
    return mapping.typeFactory.constructParametricType(this, *parameterClasses)
}

fun <T : Any> Class<T>.constructParametricType(vararg javaTypes: JavaType): JavaType {
    return mapping.typeFactory.constructParametricType(this, *javaTypes)
}