package uz.texnopos.malbazar.data.model

import com.google.gson.annotations.SerializedName

data class AddComment(
    @SerializedName("animal_id")
    val animalId: Int,
    val text: String,
    @SerializedName("user_id")
    val userId: Int
)