package uz.texnopos.malbazar.ui.main.info

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.SelectCity
import uz.texnopos.malbazar.data.helper.ResourceState
import uz.texnopos.malbazar.databinding.FragmentInfoBinding
import uz.texnopos.malbazar.ui.main.MainFragmentDirections

class InfoFragment : Fragment(R.layout.fragment_info) {

    private lateinit var b: FragmentInfoBinding
    private val args: InfoFragmentArgs by navArgs()
    private val viewModel: RecommendationViewModel by viewModel()
    private val adapter = RecommendationsAdapter()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentInfoBinding.bind(view)

        b.recyclerView.adapter = adapter
        getData(args.id)
        Glide
            .with(requireContext())
            .load(args.imgFirst)
            .into(b.ivAnimal)
        b.tvTitle.text = args.animalTitle
        b.tvDescription.text = args.description
        b.tvPrice.text = "💰  ${args.price}"
        b.tvAddress.text = "🏠  ${args.adress}"
        b.tvPhoneNumber.text = "📞  ${args.phone}"
        b.tvPhoneNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${args.phone}")
            startActivity(intent)
        }
        b.ivRight.setOnClickListener {
            Glide
                .with(requireContext())
                .load(args.imgSecond)
                .into(b.ivAnimal)
            b.ivLeft.isVisible = true
            b.ivRight.isVisible = false
        }
        b.ivLeft.setOnClickListener {
            Glide
                .with(requireContext())
                .load(args.imgFirst)
                .into(b.ivAnimal)
            b.ivLeft.isVisible = false
            b.ivRight.isVisible = true
        }
        adapter.onItemClick = {
            val city = SelectCity()
            val action = InfoFragmentDirections.actionInfoFragmentSelf(
                imgFirst = it.img1,
                imgSecond = it.img2,
                animalTitle = it.title,
                description = it.description,
                phone = it.phone,
                price = it.price,
                adress = city.selectCity(it.city_id),
                id = it.id
            )
            findNavController().navigate(action)
        }
    }

    private fun getData(animalId: Int) {
        viewModel.recommendAnimals(animalId = animalId)
        viewModel.recommendAnimals.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> {

                }
                ResourceState.SUCCESS -> {
                    adapter.models = it.data!!.likes
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}