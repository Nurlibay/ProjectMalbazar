package uz.texnopos.malbazar.ui.main.info

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
import uz.texnopos.malbazar.databinding.RecommendationItemBinding

class InfoAdapter : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {

    var onItemClick: (id: Int,categoryId:Int) -> Unit = {_,_ -> }
    var models: List<Animal> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: RecommendationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(animal: Animal) {
            val cityId = SelectCity()
            binding.tvPrice.text = "${animal.price} swm"
            binding.tvTitle.text = animal.title
            binding.tvCity.text = cityId.selectCity(animal.city_id)

            if (animal.img1 == null) {
                Glide
                    .with(binding.root.context)
                    .load(R.drawable.malbazar_logo)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(18)))
                    .into(binding.ivAnimal)
            } else {
                Glide
                    .with(binding.root.context)
                    .load(animal.img1)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(18)))
                    .into(binding.ivAnimal)
            }
            binding.constraintMainItem.setOnClickListener {
                onItemClick.invoke(animal.id,animal.category_id)
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