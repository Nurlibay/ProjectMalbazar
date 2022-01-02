package uz.texnopos.malbazar.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.SelectCity
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.databinding.MainItemBinding

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var onItemClick: (id: Int) -> Unit = {}
    var models: List<Animal> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: MainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(animal: Animal) {
            val cityId = SelectCity()
            binding.tvPrice.text = "${animal.price} swm"
            binding.tvTitle.text = animal.title
            binding.tvCity.text = cityId.selectCity(animal.city_id)

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
            binding.constraintMainItem.setOnClickListener {
                onItemClick.invoke(animal.id)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount() = models.size
}