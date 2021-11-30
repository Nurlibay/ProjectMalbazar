package uz.texnopos.malbazar.data.models

import com.google.gson.annotations.SerializedName

data class AddAnimal(
    var title: String,
    var description: String,
    @SerializedName("city_id")
    var cityId: Int,
    @SerializedName("category_id")
    var categoryId: Int,
    @SerializedName("user_id")
    var userId: Int,
    var phone: String,
    var price: String,
    var img1: String,
    var img2: String,
    var img3: String
)
