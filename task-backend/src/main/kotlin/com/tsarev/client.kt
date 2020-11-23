package com.tsarev

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.cloud.openfeign.FeignClientBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.nio.charset.Charset
import java.time.LocalDate

/**
 * Creation of API feign beans and configuring related functionality.
 */
@Configuration
open class Apis : ApplicationContextAware {

    /**
     * App context, needed for [FeignClientBuilder].
     */
    private lateinit var applicationContext: ApplicationContext
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    /**
     * Feign jackson decoder, suitable for various encodings.
     *
     * **ImplNote**: Only works with windows-1251 for now.
     */
    @Bean
    open fun jacksonDecoder() = XmlMapper(JacksonXmlModule().apply { setDefaultUseWrapper(false) })
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .let { CharsetAwareJacksonDecoder(it, Charset.forName("windows-1251")) }

    @Bean
    open fun cbrApi(): CbrApi = FeignClientBuilder(applicationContext)
        .forType(CbrApi::class.java, "cbrApi")
        .url("www.cbr.ru")
        .build()

}

/**
 * Api for interacting with cbr.ru currency rates dictionary.
 */
interface CbrApi {

    @GetMapping("scripts/XML_dynamic.asp")
    fun getCurrencyRateForPeriod(
        @RequestParam("date_req1") @DateTimeFormat(pattern = "dd/MM/YYY") dateFrom: LocalDate,
        @RequestParam("date_req2") @DateTimeFormat(pattern = "dd/MM/YYY") dateTo: LocalDate,
        @RequestParam("VAL_NM_RQ") currencyCode: String
    ): CurrencyRatePage

    @GetMapping("/scripts/XML_val.asp")
    fun getCurrencyTypes(
        @RequestParam("d") pageNumber: Int
    ): CurrencyDescriptionPage
}