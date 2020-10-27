package com.tsarev

import java.time.LocalDate

val currentMonth get() = LocalDate.now().month.value
val currentYear get() = LocalDate.now().year