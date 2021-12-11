package uz.texnopos.malbazar.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.main_item.view.*
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.data.models.Animal
import uz.texnopos.malbazar.data.models.LastAnimals
import uz.texnopos.malbazar.databinding.MainItemBinding

class MainAdapter2 : RecyclerView.Adapter<MainAdapter2.ViewHolder>() {

    var onItemClick:(animal:Animal) -> Unit = {}
    var models: List<Animal> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    inner class ViewHolder(private val binding: MainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(animal: Animal) {
            binding.tvPrice.text = "${animal.price}"
            binding.tvTitle.text = animal.title
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
            MainItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount() = models.size
}