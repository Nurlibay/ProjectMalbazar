package uz.texnopos.malbazar.ui.selected

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

class SelectedAnimalsAdapter : RecyclerView.Adapter<SelectedAnimalsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: MainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun populateModel(animal: Animal) {
            val cityId = SelectCity()
            binding.apply {
                tvPrice.text = "${animal.price} swm"
                tvTitle.text = animal.title
                binding.tvViewing.text = " ${animal.view}"
                tvCity.text = cityId.selectCity(animal.city_id)
                Glide
                    .with(root.context)
                    .load(animal.img1)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(18)))
                    .into(ivFirstAnimal)

                constraintMainItem.setOnClickListener {
                    onItemClick.invoke(animal.id,animal.category_id)
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

    var onItemClick: (id: Int, categoryId: Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount() = models.size
}