package com.dotanphu.sipapp.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dotanphu.sipapp.component.adapter.ItemUserAdapter
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.component.listener.OnItemClickListener
import com.dotanphu.sipapp.data.model.response.User
import com.dotanphu.sipapp.databinding.FragmentContactBinding
import com.dotanphu.sipapp.ui.call.OutgoingCallActivity
import com.utils.ViewUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
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
        listener()
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

        //Refresh
        ViewUtil.initSwipeRefreshLayout(getBaseActivity(), binding.swipeRefreshLayout)
    }

    private fun observe() {
        registerObserverBaseEvent(viewModel, viewLifecycleOwner)
        viewModel.loadingEvent.observe(viewLifecycleOwner) { loadingEvent ->
            if (loadingEvent.isShow) {
                if (!binding.swipeRefreshLayout.isRefreshing)
                    binding.statusLayout.showLoading()
            } else {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.statusLayout.showContent()
            }
        }

        viewModel.onUsersSuccess.observe(viewLifecycleOwner) { listUser ->
            if (!checkLiveDataState(viewLifecycleOwner)) return@observe
            mList.clear()
            mList.addAll(listUser)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun listener() {
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun getData() {
        if (!isNetworkConnected) {
            binding.swipeRefreshLayout.isRefreshing = false
            binding.statusLayout.showErrorNetwork { getData() }
            return
        }

        viewModel.getListUsers()
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = true
        getData()
    }
}