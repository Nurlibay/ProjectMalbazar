import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import uz.texnopos.malbazar.App.Companion.getAppInstance
import uz.texnopos.malbazar.core.AppBaseActivity
import java.io.File

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, text, duration).show()

fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    if (context != null) {
        context!!.toast(text, duration)
    }
}

inline fun <T : View> T.onClick(crossinline func: T.() -> Unit) = setOnClickListener { func() }

fun TextInputEditText.textToString() = this.text.toString()
fun TextView.textToString() = this.text.toString()

fun File.toRequestBody(): RequestBody =
    this.asRequestBody("image/*".toMediaTypeOrNull())

fun File.toMultiPart(key: String) =
    MultipartBody.Part.createFormData(key, this.name, this.toRequestBody())

fun Context.getConnectivityManager() =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

@RequiresApi(Build.VERSION_CODES.M)
fun Fragment.isHasPermission(permission: String): Boolean {
    return requireActivity().applicationContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}

fun Fragment.askPermission(permissions: Array<out String>, @IntRange(from = 0) requestCode: Int) =
    ActivityCompat.requestPermissions(requireActivity(), permissions, requestCode)

fun isNetworkAvailable(): Boolean {
    val info = getAppInstance().getConnectivityManager().activeNetworkInfo
    return info != null && info.isConnected
}

fun AutoCompleteTextView.showError(error: String): Boolean {
    this.error = error
    this.showSoftKeyboard()
    return false
}

fun TextInputEditText.checkIsEmpty(): Boolean = text == null ||
        textToString() == "" ||
        textToString().equals("null", ignoreCase = true)

fun AutoCompleteTextView.checkIsEmpty(): Boolean = text == null ||
        textToString() == "" ||
        textToString().equals("null", ignoreCase = true)

fun TextInputEditText.showError(error: String): Boolean {
    this.error = error
    this.showSoftKeyboard()
    return false
}

fun addLetterToWord(letter: String, word: String): String {
    return "$word$letter"
}

fun TextInputLayout.showError(error: String): Boolean {
    this.error = error
    this.showSoftKeyboard()
    return false
}

fun View.showSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun Fragment.showProgress() {
    (requireActivity() as AppBaseActivity).showProgress(true)
}

fun Fragment.hideProgress() {
    (requireActivity() as AppBaseActivity).showProgress(false)
}

fun String.getOnlyDigits(): String {
    var s = ""
    this.forEach { if (it.isDigit()) s += it }
    return s
}