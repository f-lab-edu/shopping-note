package com.chaw.shopping_note.common.converter

import org.springframework.core.convert.converter.Converter
import kotlin.reflect.KClass

class StringToEnumConverter<T : Enum<T>>(
    private val enumClass: KClass<T>
) : Converter<String, T> {
    override fun convert(source: String): T {
        return enumClass.java.enumConstants.first { it.name == source }
    }
}
