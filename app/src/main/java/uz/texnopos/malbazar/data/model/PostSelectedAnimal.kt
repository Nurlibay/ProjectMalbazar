package uz.texnopos.malbazar.data.model

import com.google.gson.annotations.SerializedName

data class PostSelectedAnimal(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("animal_id")
    val animalId: Int
)
