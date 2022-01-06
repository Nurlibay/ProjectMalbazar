package uz.texnopos.malbazar.ui.main.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import onClick
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.data.model.Category
import uz.texnopos.malbazar.databinding.CategoryItemBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(category: Category) {
            binding.apply {
                tvCategoryName.text  = category.name
                Glide
                    .with(root.context)
                    .load(category.icon)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(500)))
                    .into(ivCategory)

                categoryItem.onClick {
                    onItemClick.invoke(category.id)
                }
            }
        }
    }

    var models: List<Category> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: (id: Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryItemBinding.inflate(
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