package uz.texnopos.malbazar.data.models

data class GenericResponse<T>(
    var success: Boolean,
    var message: String,
    var data:T
)