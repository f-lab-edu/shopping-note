package com.chaw.shopping_note.common.converter

import org.springframework.core.convert.converter.Converter
import kotlin.reflect.KClass

class EnumToStringConverter<T : Enum<T>>(
    private val enumClass: KClass<T>
) : Converter<T, String> {
    override fun convert(source: T): String {
        return source.name
    }
}
