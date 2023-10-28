package com.dotanphu.sipapp.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.component.adapter.ItemUserAdapter
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.component.listener.OnItemClickListener
import com.dotanphu.sipapp.data.model.response.User
import com.dotanphu.sipapp.databinding.FragmentContactBinding
import com.dotanphu.sipapp.ui.call.OutgoingCallActivity
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment : BaseFragment() {
    companion object {
        fun newInstance(): ContactFragment {
            val args = Bundle()
            val fragment = ContactFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentContactBinding
    private lateinit var viewModel: ContactViewModel
    private lateinit var mAdapter: ItemUserAdapter

    private var mList = arrayListOf<User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        initData()
        observe()
        getData()
        return binding.root
    }

    private fun initData() {
        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]

        mAdapter = ItemUserAdapter(mList)
        mAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (!isClickable()) return
                requireActivity().startActivity(OutgoingCallActivity.newIntent(requireContext(), mList.get(position).username.toString()))
            }
        })
        binding.rvContent.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvContent.adapter = mAdapter
    }

    private fun observe() {
        registerObserverBaseEvent(viewModel, viewLifecycleOwner)
        viewModel.loadingEvent.observe(viewLifecycleOwner) { loadingEvent ->
            if (loadingEvent.isShow) {
                LogUtil.wtf("1")
                binding.statusLayout.visibility = View.VISIBLE
                binding.rvContent.visibility = View.GONE
            } else {
                LogUtil.wtf("2")
                binding.rvContent.visibility = View.VISIBLE
                binding.statusLayout.visibility = View.GONE
            }
        }
        viewModel.onUsersSuccess.observe(viewLifecycleOwner) { listUser ->
            if (!checkLiveDataState(viewLifecycleOwner)) return@observe
            mList.clear()
            mList.addAll(listUser)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun getData() {
        if (!isNetworkConnected) {
            toastError(R.string.not_connected_internet)
            return
        }

        viewModel.getListUsers()
    }
}