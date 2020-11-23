package com.tsarev

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
        @RequestParam(defaultValue = "20") pageSize: Int,
        @RequestParam substring: String?
    ) = api.getAllAvailableCurrencies()
        .filter { it.name.toUpperCase().contains(substring?.toUpperCase() ?: "") }
        .drop(if (offset < 0) 0 else offset)
        .take(if (pageSize < 0) 0 else pageSize)

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