package uz.texnopos.malbazar.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import onClick
import uz.texnopos.malbazar.core.SelectCity
import uz.texnopos.malbazar.data.model.Animal
import uz.texnopos.malbazar.databinding.MainItemBinding

class AdapterLastAdded : RecyclerView.Adapter<AdapterLastAdded.ViewHolder>() {

    inner class ViewHolder(private val binding: MainItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun populateModel(animal: Animal) {
            binding.apply {
                val cityId = SelectCity()
                tvPrice.text = "${animal.price} swm"
                tvTitle.text = animal.title
                tvCity.text = cityId.selectCity(animal.city_id)
                Glide
                    .with(root.context)
                    .load(animal.img1)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(18)))
                    .into(ivFirstAnimal)
                constraintMainItem.onClick {
                    onItemClick.invoke(animal.id)
                }
            }
        }
    }

    var models: List<Animal> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: (id: Int) -> Unit = {}
    fun setOnItemClickListener(onItemClick: (id: Int) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MainItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount() = models.size
}