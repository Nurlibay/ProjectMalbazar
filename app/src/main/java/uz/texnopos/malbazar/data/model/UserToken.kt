package uz.texnopos.malbazar.data.model

import com.google.gson.annotations.SerializedName

data class UserToken(
    var token: String,
    @SerializedName("user_id")
    var userId: Int
)
