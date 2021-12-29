package uz.texnopos.malbazar.data.models

import com.google.gson.annotations.SerializedName

data class SearchAnimal(
    var query: String,
    @SerializedName("city_id")
    var cityId: String,
    @SerializedName("category_id")
    var categotyId: String
)
