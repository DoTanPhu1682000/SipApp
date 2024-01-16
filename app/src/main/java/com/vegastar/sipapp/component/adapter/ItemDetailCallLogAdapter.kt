package com.vegastar.sipapp.component.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vegastar.sipapp.R
import com.vegastar.sipapp.databinding.RowItemDetailCallLogBinding
import com.vegastar.sipapp.utils.TimestampUtils
import com.vegastar.sipapp.utils.core.CoreHelper
import org.linphone.core.Call
import org.linphone.core.CallLog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ItemDetailCallLogAdapter(private val context: Context, private var mList: List<CallLog>) : RecyclerView.Adapter<ItemDetailCallLogAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RowItemDetailCallLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ItemViewHolder(val binding: RowItemDetailCallLogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(callLog: CallLog) {
            binding.tvTime.text = TimestampUtils.toString(callLog.startDate, shortDate = false, hideYear = false)
            binding.tvTimeCall.text = formatTime(callLog)

            if (callLog.dir == Call.Dir.Incoming) {
                if (CoreHelper.getInstance(context)?.isCallLogMissed(callLog) == true) {
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
        }
    }

    private fun formatTime(callLog: CallLog): String {
        val dateFormat = SimpleDateFormat(
            if (callLog.duration >= 3600) "HH:mm:ss" else "mm:ss", Locale.getDefault()
        )
        val cal = Calendar.getInstance()
        cal[0, 0, 0, 0, 0] = callLog.duration
        return dateFormat.format(cal.time)
    }
}