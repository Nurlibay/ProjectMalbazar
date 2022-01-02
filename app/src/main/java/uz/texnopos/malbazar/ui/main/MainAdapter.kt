package uz.texnopos.malbazar.ui.main

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.SelectCity
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.databinding.MainItemBinding
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.Toast
import androidx.annotation.RequiresApi


class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var onPhoneClick: (animal: Animal) -> Unit = {}
    var toast: (word: String) -> Unit = {}
    var models: List<Animal> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: MainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun populateModel(animal: Animal) {
            val cityId = SelectCity()
            binding.tvPrice.text = "${animal.price} swm"
            binding.tvTitle.text = animal.title
            binding.tvDescription.text = animal.description
            binding.tvPhoneNumber.text = animal.phone
            binding.tvCity.text = cityId.selectCity(animal.city_id)
            binding.horScrollView.smoothScrollBy(0, 0)

            if (animal.img1.isEmpty()) {
                Glide
                    .with(binding.root.context)
                    .load(R.drawable.malbazar_logo)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(18)))
                    .into(binding.ivFirstAnimal)
            } else {
                Glide
                    .with(binding.root.context)
                    .load(animal.img1)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(18)))
                    .into(binding.ivFirstAnimal)
            }
            binding.tvPhoneNumber.setOnClickListener {
                onPhoneClick.invoke(animal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MainItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount() = models.size
}