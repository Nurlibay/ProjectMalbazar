package uz.texnopos.malbazar.ui.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var b: FragmentCategoryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentCategoryBinding.bind(view)
        b.constraint1.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentForMainToMainFragment(1)
            findNavController().navigate(action)
        }
        b.constraint2.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentForMainToMainFragment(2)
            findNavController().navigate(action)
        }
        b.constraint3.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentForMainToMainFragment(3)
            findNavController().navigate(action)
        }
        b.constraint4.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentForMainToMainFragment(4)
            findNavController().navigate(action)
        }
        b.constraint5.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentForMainToMainFragment(5)
            findNavController().navigate(action)
        }
        b.constraint6.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentForMainToMainFragment(6)
            findNavController().navigate(action)
        }
        b.constraint7.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentForMainToMainFragment(7)
            findNavController().navigate(action)
        }
        b.constraint8.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentForMainToMainFragment(8)
            findNavController().navigate(action)
        }
        b.constraint9.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentForMainToMainFragment(9)
            findNavController().navigate(action)
        }
    }
}