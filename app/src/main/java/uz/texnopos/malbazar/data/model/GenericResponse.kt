package uz.texnopos.malbazar.data.model

data class GenericResponse<T>(
    var success: Boolean,
    var message: String,
    var data: T
)