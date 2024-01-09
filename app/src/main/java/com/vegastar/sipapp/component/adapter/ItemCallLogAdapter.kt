package com.vegastar.sipapp.component.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vegastar.sipapp.R
import com.vegastar.sipapp.component.listener.OnItemClickListener
import com.vegastar.sipapp.databinding.RowItemCallLogBinding
import com.vegastar.sipapp.ui.history.data.GroupedCallLogData
import com.vegastar.sipapp.utils.TimestampUtils
import com.vegastar.sipapp.utils.core.CoreHelper
import org.linphone.core.Call

class ItemCallLogAdapter(private val context: Context, private var mList: List<GroupedCallLogData>) : RecyclerView.Adapter<ItemCallLogAdapter.ItemViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RowItemCallLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ItemViewHolder(val binding: RowItemCallLogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(callLogGroup: GroupedCallLogData) {
            binding.tvUserName.text = "${callLogGroup.lastCallLog.remoteAddress.displayName}"
            binding.tvDescription.text = "${callLogGroup.lastCallLog.remoteAddress.username}"
            binding.tvTime.text = formatDate(context, callLogGroup.lastCallLogStartTimestamp)

            if (callLogGroup.lastCallLog.dir == Call.Dir.Incoming) {
                if (CoreHelper.getInstance(context)?.isCallLogMissed(callLogGroup.lastCallLog) == true) {
                    Glide.with(context)
                        .load(R.drawable.call_status_missed)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.imgCall)
                } else {
                    Glide.with(context)
                        .load(R.drawable.call_status_incoming)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.imgCall)
                }
            } else {
                Glide.with(context)
                    .load(R.drawable.call_status_outgoing)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.imgCall)
            }

            binding.root.setOnClickListener {
                mOnItemClickListener?.onItemClick(adapterPosition)
            }
        }
    }

    private fun formatDate(context: Context, date: Long): String {
        if (TimestampUtils.isToday(date)) {
            return context.getString(R.string.today)
        } else if (TimestampUtils.isYesterday(date)) {
            return context.getString(R.string.yesterday)
        }
        return TimestampUtils.toString(date, onlyDate = true, shortDate = false, hideYear = false)
    }
}