package com.vegastar.sipapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.utils.LogUtil
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCallLogBinding.inflate(inflater, container, false)
        initData()
        getData()
        return binding.root
    }

    private fun initData() {
        mAdapter = ItemCallLogAdapter(requireContext(), mList)
        binding.rvContent.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvContent.adapter = mAdapter
    }

    private fun getData() {
        val allCallLogs = CoreHelper.getInstance(requireContext())?.core?.callLogs
        LogUtil.wtf("${allCallLogs?.size} call logs found")

        mList = when (filter) {
            CallLogsFilter.MISSED -> computeCallLogs(allCallLogs!!, missed = true, conference = false)
            CallLogsFilter.CONFERENCE -> computeCallLogs(allCallLogs!!, missed = false, conference = true)
            else -> computeCallLogs(allCallLogs!!, missed = false, conference = false)
        }
    }

    private fun computeCallLogs(callLogs: Array<CallLog>, missed: Boolean, conference: Boolean): ArrayList<GroupedCallLogData> {
        var previousCallLogGroup: GroupedCallLogData? = null

        for (callLog in callLogs) {
            if ((!missed && !conference) || (missed && CoreHelper.getInstance(requireContext())
                    ?.isCallLogMissed(callLog) == true) || (conference && callLog.wasConference())
            ) {
                if (previousCallLogGroup == null) {
                    previousCallLogGroup = GroupedCallLogData(callLog)
                } else if (!callLog.wasConference() && // Do not group conference call logs
                    callLog.wasConference() == previousCallLogGroup.lastCallLog.wasConference() && // Check that both are of the same type, if one has a conf-id and not the other the equal method will return true !
                    previousCallLogGroup.lastCallLog.localAddress.weakEqual(callLog.localAddress) && previousCallLogGroup.lastCallLog.remoteAddress.equal(callLog.remoteAddress)
                ) {
                    if (TimestampUtils.isSameDay(previousCallLogGroup.lastCallLogStartTimestamp, callLog.startDate
                        )
                    ) {
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

        LogUtil.wtf(mList.size.toString())
        LogUtil.`object`(mList)

        return mList
    }
}

enum class CallLogsFilter {
    ALL, MISSED, CONFERENCE
}