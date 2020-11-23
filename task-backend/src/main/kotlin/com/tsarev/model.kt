package com.tsarev

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.time.LocalDate

/**
 * Single currency description.
 */
@JacksonXmlRootElement(localName = "Item")
class CurrencyDescription {

    @set:JacksonXmlProperty(isAttribute = true, localName = "ID")
    lateinit var code: String

    @set:JsonProperty("Name")
    @get:JsonProperty("name")
    var name: String = ""
}

/**
 * Single currency description page.
 */
@JacksonXmlRootElement(localName = "Valuta")
class CurrencyDescriptionPage {

    @set:JsonProperty("Item")
    var items: List<CurrencyDescription> = emptyList()
}

/**
 * Currency rate for single day.
 */
@JacksonXmlRootElement(localName = "Record")
class CurrencyRate {

    @set:JacksonXmlProperty(isAttribute = true, localName = "Id")
    lateinit var code: String

    @get:JsonFormat(pattern = "dd.MM.yyyy")
    @set:JacksonXmlProperty(isAttribute = true, localName = "Date")
    lateinit var date: LocalDate

    @set:JsonDeserialize(using = CommaSeparatedPrimitiveDoubleDeserializer::class)
    @set:JsonProperty("Value")
    @get:JsonProperty("rate")
    var rate: Double = Double.NaN
}

/**
 * Currency rate page.
 */
@JacksonXmlRootElement(localName = "ValCurs")
class CurrencyRatePage {

    @set:JsonProperty("Record")
    var items: List<CurrencyRate> = emptyList()

}