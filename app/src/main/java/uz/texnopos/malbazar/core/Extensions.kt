package uz.texnopos.malbazar.core

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) = setOnClickListener { func() }

fun Int.toRequestBody(toIntOrNull: Int?): RequestBody =
    this.toRequestBody("int/plain".toIntOrNull())

fun String.toRequestBody(): RequestBody =
    this.toRequestBody("text/plain".toMediaTypeOrNull())

fun File.toRequestBody(): RequestBody =
    this.asRequestBody("image/*".toMediaTypeOrNull())

fun File.toMultiPart(key: String) =
    MultipartBody.Part.createFormData(key, this.name, this.toRequestBody())
fun ImageView.setLocalImage(uri: Uri, applyCircle: Boolean = false) {
    val glide = Glide.with(this).load(uri)
    if (applyCircle) {
        glide.apply(RequestOptions.circleCropTransform()).into(this)
    } else {
        glide.into(this)
    }
}