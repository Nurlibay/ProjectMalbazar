package uz.texnopos.malbazar.data.model

import com.google.gson.annotations.SerializedName

data class SelectedAnimal(
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("city_id")
    val cityId: Int,
    @SerializedName("favourite_id")
    val favouriteId: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val description: String,
    val id: Int,
    val img1: String,
    val img2: String,
    val img3: String,
    val phone: String,
    val price: String,
    val title: String,
    val top: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int,
    val view: Int
)