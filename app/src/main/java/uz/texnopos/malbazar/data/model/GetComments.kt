package uz.texnopos.malbazar.data.model

import com.google.gson.annotations.SerializedName

data class GetComments(
    @SerializedName("animal_id")
    val animalId: Int,
    @SerializedName("crated_at")
    val createdAt: String,
    val id: Int,
    val text: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_name")
    val userName: String
)