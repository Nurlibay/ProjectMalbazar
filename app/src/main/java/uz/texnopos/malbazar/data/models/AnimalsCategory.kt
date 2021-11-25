package uz.texnopos.malbazar.data.models

import com.google.gson.annotations.SerializedName

data class AnimalsCategory(
    val id:Int,
    val name:String,
    val icon:String,
    @SerializedName("created_at")
    val createdAt:String,
    @SerializedName("updated_at")
    val updatedAt:String

)
