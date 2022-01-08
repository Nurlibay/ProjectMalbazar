package uz.texnopos.malbazar.ui.aboutUs

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import onClick
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.databinding.FragmentAboutUsBinding
import android.content.Intent
import android.net.Uri
import android.os.Handler
import askPermission
import isHasPermission
import uz.texnopos.malbazar.core.Constants

class AboutUsFragment : Fragment(R.layout.fragment_about_us) {

    private lateinit var binding: FragmentAboutUsBinding
    private val mapUri: String = "https://goo.gl/maps/Uh5R96JyMFr89dWg7"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAboutUsBinding.bind(view)
        binding.apply {
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            locateContainer.onClick {
                Handler().postDelayed(Runnable {
                    val gmmIntentUri = Uri.parse(mapUri)
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                }, 1000)
            }
            phoneContainer.onClick {
                makeCall()
            }
            emailContainer.onClick {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "info@malbazar.uz", null))
                intent.putExtra(Intent.EXTRA_SUBJECT, "")
                intent.putExtra(Intent.EXTRA_TEXT, "")
                startActivity(Intent.createChooser(intent, "Choose an Email client :"))
            }
            telegramContainer.onClick {
                val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/malbazar_chat"))
                startActivity(telegram)
            }
            instagramContainer.onClick {
                toast(context.getString(R.string.no_instagram_account))
            }
            facebookContainer.onClick {
                toast(context.getString(R.string.no_facebook_account))
            }
            tiktokContainer.onClick {
                toast(context.getString(R.string.no_tiktok_account))
            }
        }

    }

    private fun makeCall() {
        if (isHasPermission(Manifest.permission.CALL_PHONE)) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:${+334470069}")
            startActivity(callIntent)
        } else askPermission(
            arrayOf(Manifest.permission.CALL_PHONE),
            Constants.ASK_PHONE_PERMISSION_REQUEST_CODE
        )
    }
}