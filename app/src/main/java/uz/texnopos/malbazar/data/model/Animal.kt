package uz.texnopos.malbazar.data.model

import com.google.gson.annotations.SerializedName

data class Animal(
    val category_id: Int,
    val city_id: Int,
    val created_at: String,
    val description: String,
    val id: Int,
    val img1: String,
    val img2: String,
    val img3: String,
    val phone: String,
    val price: String,
    val title: String,
    val top: Int,
    val updated_at: String,
    val user_id: Int,
    val view: Int,
    @SerializedName("category_name")
    val categoryName:String
)