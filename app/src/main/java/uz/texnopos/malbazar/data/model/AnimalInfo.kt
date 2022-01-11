package uz.texnopos.malbazar.data.model

data class AnimalInfo(
    val animal: Animal,
    val likes: List<Animal>,
    val commentsCount:Int
)