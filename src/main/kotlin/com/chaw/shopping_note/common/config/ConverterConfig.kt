package com.chaw.shopping_note.common.config

import com.chaw.shopping_note.app.receipt.domain.Category
import com.chaw.shopping_note.app.receipt.domain.StoreType
import com.chaw.shopping_note.common.converter.EnumToStringConverter
import com.chaw.shopping_note.common.converter.StringToEnumConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.CustomConversions
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions

@Configuration
class ConverterConfig {

    @Bean
    fun r2dbcCustomConversions(): R2dbcCustomConversions {
        val converters = mutableListOf<Converter<*, *>>()

        // Category <-> String
        converters.add(EnumToStringConverter(Category::class))
        converters.add(StringToEnumConverter(Category::class))

        // StoreType <-> String
        converters.add(EnumToStringConverter(StoreType::class))
        converters.add(StringToEnumConverter(StoreType::class))

        return R2dbcCustomConversions(CustomConversions.StoreConversions.NONE, converters)
    }
}
