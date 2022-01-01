package uz.texnopos.malbazar.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.texnopos.malbazar.SelectCity
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.databinding.MainItem2Binding

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var onItemClick:(animal:Animal) -> Unit = {}
    var models: List<Animal> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    inner class ViewHolder(private val binding: MainItem2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(animal: Animal) {
            val cityId = SelectCity()
            binding.tvPrice.text = "${animal.price} swm"
            binding.tvTitle.text = animal.title
            binding.tvCity.text = cityId.selectCity(animal.city_id)
            Glide
                .with(binding.root.context)
                .load(animal.img1)
                .into(binding.ivAnimal)
            binding.constraintMainItem.setOnClickListener {
            onItemClick.invoke(animal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MainItem2Binding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount() = models.size
}