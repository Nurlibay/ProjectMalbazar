package uz.texnopos.malbazar.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.databinding.FragmentAddBinding

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var b: FragmentAddBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentAddBinding.bind(view)
        b.menu.setOnClickListener {
            showPopup(it)
        }
        b.menuCity.setOnClickListener {
        }
    }

    fun showPopup(v: View) {
        val popup = PopupMenu(v.context, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.category_menu, popup.menu)
        popup.show()
    }
}