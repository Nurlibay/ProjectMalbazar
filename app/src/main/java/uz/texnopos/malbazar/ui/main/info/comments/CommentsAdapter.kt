package uz.texnopos.malbazar.ui.main.info.comments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.malbazar.data.model.GetComments
import uz.texnopos.malbazar.databinding.CommentItemBinding

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")

        fun populateModel(comment: GetComments) {
            binding.apply {
                binding.tvName.text = comment.userName
                binding.tvMessage.text = comment.text
            }
        }
    }

    var models: List<GetComments> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.populateModel(models[position]) }

    override fun getItemCount() = models.size
}