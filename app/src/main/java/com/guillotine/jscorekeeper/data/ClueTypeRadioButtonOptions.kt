package com.guillotine.jscorekeeper.data

import com.guillotine.jscorekeeper.R

enum class ClueTypeRadioButtonOptions(val stringResourceID: Int) {
    CORRECT(R.string.correct),
    INCORRECT(R.string.incorrect),
    PASS(R.string.pass),
    DAILY_DOUBLE(R.string.daily_double)
}