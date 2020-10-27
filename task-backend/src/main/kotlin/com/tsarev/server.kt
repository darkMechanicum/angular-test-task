package com.tsarev

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

/**
 * Handlers for outer requests.
 */
@RestController
open class OnlineTest(
    private val api: CachedApi
) {

    /**
     * Get currencies types, paged.
     */
    @GetMapping("/currencies")
    open fun getCurrenciesPaged(
        @RequestParam(defaultValue = "0") offset: Int,
        @RequestParam(defaultValue = "20") limit: Int
    ) = api.getAllAvailableCurrencies()
        .drop(if (offset < 0) 0 else offset)
        .take(if (limit < 0) 0 else limit)

    /**
     * Get month currency rate.
     */
    @GetMapping("/rates/{code}")
    open fun getCurrenciesRate(
        @RequestParam year: Int?,
        @RequestParam month: Int?,
        @PathVariable code: String
    ) = api.getMonthCurrenciesRates(
        year ?: currentYear,
        (month ?: currentMonth).let { if (it in 1..12) it else currentMonth },
        code
    )

}