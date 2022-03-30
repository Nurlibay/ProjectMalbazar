package uz.texnopos.malbazar.ui.main.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
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
        @SuppressLint("SetTextI18n")
        fun populateModel(category: Category) {
            binding.apply {
                tvCategoryName.text = category.name
                Glide
                    .with(root.context)
                    .load(category.icon)
                    .into(ivCategory)
                ivCategory.setBackgroundResource(R.drawable.shapeable_category)
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
    var onItemClick: (categoryId: Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount() = models.size

    fun addCategory(category: Category) {
        val list = models.toMutableList()
        list.add(0, category)
        models = list
        notifyItemInserted(list.indexOf(category))
    }
}