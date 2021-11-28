package uz.texnopos.malbazar.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.databinding.FragmentAddBinding

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var b: FragmentAddBinding
    private val args: AddFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentAddBinding.bind(view)
        when (args.id) {
            0 -> b.menu.text = "Bólimler"
            1 -> b.menu.text = "Qaramal"
            2 -> b.menu.text = "Tuye"
            3 -> b.menu.text = "Jılqı"
            4 -> b.menu.text = "Eshki"
            5 -> b.menu.text = "Qoy"
            6 -> b.menu.text = "Mal azıǵı"
            7 -> b.menu.text = "Tawıq"
            8 -> b.menu.text = "Mal tasıw hizmeti"
            9 -> b.menu.text = "Teri(satıw hám satıp alıw)"
        }
        b.menu.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_categoryFragment)
        }
        b.tvCity.setOnClickListener {
            showPopup(it)
        }

        b.btnSet.setOnClickListener {
            when {
                b.menu.text.isEmpty() -> {
                    Toast.makeText(requireContext(), "Bólimdi saylań", Toast.LENGTH_SHORT).show()
                }
                b.etShortInfo.text.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Qısqasha malıwmat kiritıń",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                b.etInfo.text.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Tolıǵraq malıwmat kiritıń",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                b.etPrice.text.isEmpty() -> {
                    b.etPrice.error = ""
                }
                b.etPhone.text.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Telefon nomerıńızdı kiritıń",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                b.tvCity.text.isEmpty() -> {
                    b.tvCity.error = ""
                }
                else -> {
                    setData()
                }
            }
        }
    }

    private fun setData() {

    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.city_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.nokis -> {
                    b.tvCity.text = item.title
                }
                R.id.shimbay -> {
                    b.tvCity.text = item.title
                }
                R.id.qonirat -> {
                    b.tvCity.text = item.title
                }
                R.id.xojeli -> {
                    b.tvCity.text = item.title
                }
                R.id.taxtakopir -> {
                    b.tvCity.text = item.title
                }
                R.id.moynaq -> {
                    b.tvCity.text = item.title
                }
                R.id.nokisRayon -> {
                    b.tvCity.text = item.title
                }
                R.id.kegeyli -> {
                    b.tvCity.text = item.title
                }
                R.id.qanlikol -> {
                    b.tvCity.text = item.title
                }
                R.id.shomanay -> {
                    b.tvCity.text = item.title
                }
                R.id.amudarya -> {
                    b.tvCity.text = item.title
                }
                R.id.beruniy -> {
                    b.tvCity.text = item.title
                }
                R.id.tortkol -> {
                    b.tvCity.text = item.title
                }
                R.id.elliqqala -> {
                    b.tvCity.text = item.title
                }
                R.id.bozataw -> {
                    b.tvCity.text = item.title
                }
                R.id.qaraozek -> {
                    b.tvCity.text = item.title
                }
                R.id.taqiatas -> {
                    b.tvCity.text = item.title
                }
            }
            true
        }
        popup.show()
    }
}