package uz.texnopos.malbazar.data.model

data class Ad(
    val city_name: String,
    val description: String,
    val id: Int,
    val img1: String,
    val img2: Any,
    val img3: Any,
    val price: String,
    val title: String,
    val top: Int,
    val user_id: Int,
    val view: Int
)