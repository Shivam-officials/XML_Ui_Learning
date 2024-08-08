package com.shivam.xmluilearning.model

//todo doubt why this is only working when its nullable
data class School(
    val id : String? = null,
    var name: String? = null,
    var address: String? = null,
    var ratings: String? = null,
    var ratingCount: String? = null,
    var schoolImageUrl: String? = null
)
