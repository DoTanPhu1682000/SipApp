package com.vegastar.sipapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.utils.LogUtil
import com.vegastar.sipapp.R
import com.vegastar.sipapp.component.adapter.ItemCallLogAdapter
import com.vegastar.sipapp.component.base.BaseFragment
import com.vegastar.sipapp.databinding.FragmentCallLogBinding
import com.vegastar.sipapp.ui.history.data.GroupedCallLogData
import com.vegastar.sipapp.utils.TimestampUtils
import com.vegastar.sipapp.utils.core.CoreHelper
import dagger.hilt.android.AndroidEntryPoint
import org.linphone.core.CallLog

@AndroidEntryPoint
class CallLogFragment : BaseFragment() {

    companion object {
        fun newInstance(): CallLogFragment {
            val args = Bundle()
            val fragment = CallLogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentCallLogBinding
    private lateinit var mAdapter: ItemCallLogAdapter

    private var mList = arrayListOf<GroupedCallLogData>()
    private var filter: CallLogsFilter? = null
    private var type: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCallLogBinding.inflate(inflater, container, false)
        initData()
        listener()
        getData()
        return binding.root
    }

    private fun initData() {
        mAdapter = ItemCallLogAdapter(requireContext(), mList)
        binding.rvContent.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvContent.adapter = mAdapter

        checkCallLogAllOrMissed()
    }

    private fun listener() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.bAllCallLog.setOnClickListener {
            showAllCallLogs()
        }

        binding.bMissedCallLog.setOnClickListener {
            showOnlyMissedCallLogs()
        }
    }

    private fun getData() {
        showAllCallLogs()
    }

    private fun showAllCallLogs() {
        mList.clear()
        filter = CallLogsFilter.ALL
        type = 0
        checkCallLogAllOrMissed()
        updateCallLogs()
    }

    private fun showOnlyMissedCallLogs() {
        mList.clear()
        filter = CallLogsFilter.MISSED
        type = 1
        checkCallLogAllOrMissed()
        updateCallLogs()
    }

    private fun updateCallLogs() {
        val allCallLogs = CoreHelper.getInstance(requireContext())?.core?.callLogs
        LogUtil.wtf("${allCallLogs?.size} call logs found")

        mList = when (filter) {
            CallLogsFilter.MISSED -> computeCallLogs(allCallLogs!!, missed = true, conference = false)
            CallLogsFilter.CONFERENCE -> computeCallLogs(allCallLogs!!, missed = false, conference = true)
            else -> computeCallLogs(allCallLogs!!, missed = false, conference = false)
        }

        mAdapter.notifyDataSetChanged()
    }

    private fun computeCallLogs(callLogs: Array<CallLog>, missed: Boolean, conference: Boolean): ArrayList<GroupedCallLogData> {
        var previousCallLogGroup: GroupedCallLogData? = null

        for (callLog in callLogs) {
            if ((!missed && !conference) || (missed && CoreHelper.getInstance(requireContext())?.isCallLogMissed(callLog) == true) || (conference && callLog.wasConference())) {
                if (previousCallLogGroup == null) {
                    previousCallLogGroup = GroupedCallLogData(callLog)
                } else if (!callLog.wasConference() && // Do not group conference call logs
                    callLog.wasConference() == previousCallLogGroup.lastCallLog.wasConference() && // Check that both are of the same type, if one has a conf-id and not the other the equal method will return true !
                    previousCallLogGroup.lastCallLog.localAddress.weakEqual(callLog.localAddress) && previousCallLogGroup.lastCallLog.remoteAddress.equal(callLog.remoteAddress)
                ) {
                    if (TimestampUtils.isSameDay(previousCallLogGroup.lastCallLogStartTimestamp, callLog.startDate)) {
                        previousCallLogGroup.callLogs.add(callLog)
                        previousCallLogGroup.updateLastCallLog(callLog)
                    } else {
                        mList.add(previousCallLogGroup)
                        previousCallLogGroup = GroupedCallLogData(callLog)
                    }
                } else {
                    mList.add(previousCallLogGroup)
                    previousCallLogGroup = GroupedCallLogData(callLog)
                }
            }
        }
        if (previousCallLogGroup != null && !mList.contains(previousCallLogGroup)) {
            mList.add(previousCallLogGroup)
        }

        LogUtil.wtf("Item history: ${mList.size}")
        LogUtil.`object`(mList)

        return mList
    }

    private fun checkCallLogAllOrMissed() {
        if (type == 0) {
            binding.bAllCallLog.setBackgroundResource(R.drawable.bg_call_log_select)
            binding.bMissedCallLog.setBackgroundResource(R.drawable.bg_call_log_unselect)
            binding.bAllCallLog.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            binding.bMissedCallLog.setTextColor(ContextCompat.getColor(requireContext(), R.color.ink300))
        } else {
            binding.bAllCallLog.setBackgroundResource(R.drawable.bg_call_log_unselect)
            binding.bMissedCallLog.setBackgroundResource(R.drawable.bg_call_log_select)
            binding.bAllCallLog.setTextColor(ContextCompat.getColor(requireContext(), R.color.ink300))
            binding.bMissedCallLog.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        }
    }
}

enum class CallLogsFilter {
    ALL, MISSED, CONFERENCE
}