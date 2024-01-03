package com.dotanphu.sipapp.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotanphu.sipapp.component.listener.OnItemClickListener
import com.dotanphu.sipapp.data.model.response.User
import com.dotanphu.sipapp.databinding.RowItemUsersBinding

class ItemUserAdapter(private var mList: List<User>) : RecyclerView.Adapter<ItemUserAdapter.ItemViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RowItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ItemViewHolder(val binding: RowItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(user: User) {
            binding.tvUserName.text = "${user.username}"
            binding.tvDescription.text = "${user.description}"

            binding.root.setOnClickListener {
                mOnItemClickListener?.onItemClick(adapterPosition)
            }
        }
    }

    fun updateData(newList: List<User>) {
        mList = newList
        notifyDataSetChanged()
    }
}