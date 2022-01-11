package uz.texnopos.malbazar.ui.main.info

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.SelectCity
import uz.texnopos.malbazar.data.model.Animal
import uz.texnopos.malbazar.databinding.RecommendationItemBinding

class InfoAdapter : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {

    var onItemClick: (id: Int) -> Unit = {}
    var models: List<Animal> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: RecommendationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun populateModel(animal: Animal) {
            val cityId = SelectCity()
            binding.apply {
                tvPrice.text = "${animal.price} swm"
                tvTitle.text = animal.title
                tvCity.text = cityId.selectCity(animal.city_id)

                Glide
                    .with(root.context)
                    .load(animal.img1)
                    .into(binding.ivAnimal)
                constraintMainItem.setOnClickListener {
                    onItemClick.invoke(animal.id)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecommendationItemBinding.inflate(
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