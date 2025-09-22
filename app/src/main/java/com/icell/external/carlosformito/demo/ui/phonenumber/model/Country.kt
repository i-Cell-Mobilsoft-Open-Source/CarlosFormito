package com.icell.external.carlosformito.demo.ui.phonenumber.model

import com.icell.external.carlosformito.demo.R

enum class Country(
    val selectionTextRes: Int,
    val callCode: String
) {
    USA(R.string.country_usa, "+1"),
    DE(R.string.country_de, "+49"),
    HU(R.string.country_hu, "+36"),
    ES(R.string.country_es, "+34"),
    JP(R.string.country_jp, "+81"),
    TST(R.string.country_test, "+888")
}
