package uz.texnopos.malbazar.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import uz.texnopos.malbazar.core.SelectCity
import uz.texnopos.malbazar.data.model.Animal
import uz.texnopos.malbazar.databinding.MainItemBinding

class AdapterLastAdded : RecyclerView.Adapter<AdapterLastAdded.ViewHolder>() {

    var models: List<Animal> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: MainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun populateModel(animal: Animal) {
            val cityId = SelectCity()
            binding.tvPrice.text = animal.price
            binding.tvTitle.text = animal.title
            binding.tvViewing.text = " ${animal.view}"
            binding.tvCity.text = cityId.selectCity(animal.city_id)
            Glide
                .with(binding.root.context)
                .load(animal.img1)
                .into(binding.ivFirstAnimal)

            binding.constraintMainItem.setOnClickListener {
                onItemClick.invoke(it.id)
            }
        }
    }

    var onItemClick: (id: Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( MainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.populateModel(models[position]) }

    override fun getItemCount() = models.size
}