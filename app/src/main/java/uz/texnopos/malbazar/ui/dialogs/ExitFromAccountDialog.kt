package uz.texnopos.malbazar.ui.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import uz.texnopos.malbazar.databinding.ExitDialogBinding

class ExitFromAccountDialog(mContext: Context) : AlertDialog(mContext) {

    private lateinit var binding: ExitDialogBinding
    var exitClick: () -> Unit = {}

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExitDialogBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        binding.tvExit.setOnClickListener {
            exitClick.invoke()
            dismiss()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }
}