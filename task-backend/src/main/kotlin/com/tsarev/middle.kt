package com.tsarev

import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.concurrent.CompletableFuture

/**
 * "Middle" service where APIs answers are transformed into suitable
 * for convenient caching.
 */
@Service
open class CachedApi(
    private val cbrApi: CbrApi
) {

    @Cacheable("currenciesCache")
    open fun getAllAvailableCurrencies(): List<CurrencyDescription>{
        var latch = 100;
        var pageNumber = 0;
        lateinit var currenices: List<CurrencyDescription>
        val allCurrencies = mutableListOf<CurrencyDescription>()
        do {
            currenices = cbrApi.getCurrencyTypes(pageNumber++).items
            allCurrencies += currenices
        } while (--latch > 0 && currenices.isNotEmpty())
        return allCurrencies.sortedBy { it.name }
    }

    @Cacheable("monthCurrenciesRateCache")
    open fun getMonthCurrenciesRates(
        year: Int,
        month: Int,
        code: String
    ): List<CurrencyRate> {
        val firstDayOfMonth = LocalDate.of(year, month, 1)
        val lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1)
        return cbrApi.getCurrencyRateForPeriod(firstDayOfMonth, lastDayOfMonth, code).items
    }
}

/**
 * Class that warms cahes when application is ready for operating.
 *
 * Warming earlier is not possible by simple means, since caching implementation
 * is inactive during ctx activation.
 */
@Service
open class CacheWarmer(
    private val api: CachedApi
) : ApplicationListener<ApplicationReadyEvent> {

    companion object {
        val logger = LoggerFactory.getLogger(CacheWarmer::class.java)
    }

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        // Do warming async instead of blocking other listeners.
        CompletableFuture.runAsync {
            // Warm currencies dictionary.
            val firstCurrencies = api.getAllAvailableCurrencies()
                .take(20)
            logger.info("Finished warming cache for \"getAllAvailableCurrencies\".", )

            // Warm currencies rates for first 50 currencies and current year
            (1..12).forEach { month ->
                firstCurrencies.forEach { currency ->
                    api.getMonthCurrenciesRates(currentYear, month, currency.code)
                }
            }
            logger.info("Finished warming cache for \"getMonthCurrenciesRates\".", )
        }
    }
}