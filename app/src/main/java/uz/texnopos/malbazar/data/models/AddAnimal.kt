package uz.texnopos.malbazar.data.models

import com.google.gson.annotations.SerializedName
import java.io.File

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
    var img1: File?,
    var img2: File?,
    var img3: File?
)