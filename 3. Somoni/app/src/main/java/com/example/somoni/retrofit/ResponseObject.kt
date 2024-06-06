package com.example.somoni.retrofit

import com.google.gson.annotations.SerializedName

class Colors {
    @SerializedName("color_1")
    var firstColor: String? = null
    @SerializedName("color_2")
    var secondColor: String? = null
}
class Currency {
    var name: String? = null
    @SerializedName("full_name")
    var fullName: String? = null
    var flag: String? = null
    @SerializedName("sell_value")
    var sellValue: String? = null
    @SerializedName("buy_value")
    var buyValue: String? = null
}

data class ResponseObject (
    @SerializedName("bank_name")
    var bankName: String? = null,
    @SerializedName("ShortName")
    var shortName: String? = null,
    var colors: Colors? = null,
    var icon: String? = null,
    @SerializedName("is_change")
    var isChange: Boolean = false,
    @SerializedName("android_link")
    var androidLink: String? = null,
    @SerializedName("ios_link")
    var iosLink: String? = null,
    @SerializedName("Currency")
    var currency: ArrayList<Currency>? = null
)

data class NBTResponse(
    var id: String? = null,
    var name: String? = null,
    var nominal: Int = 0,
    var full_name: String? = null,
    var flag: String? = null,
    var value: Double = 0.0
)
