package com.vegastar.sipapp.ui.home.contact

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.utils.LogUtil
import com.utils.ViewUtil
import com.vegastar.sipapp.component.adapter.ItemUserAdapter
import com.vegastar.sipapp.component.base.BaseFragment
import com.vegastar.sipapp.component.listener.OnItemClickListener
import com.vegastar.sipapp.data.model.response.User
import com.vegastar.sipapp.databinding.FragmentContactBinding
import com.vegastar.sipapp.ui.call.OutgoingCallActivity
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
                val selectedUser = mAdapter.getItem(position)
                LogUtil.wtf("fcmToken: ${selectedUser.description}")
                if (selectedUser.description!!.isNotEmpty()) {
                    requireActivity().startActivity(OutgoingCallActivity.newIntent(requireContext(), selectedUser.username, selectedUser.displayName, selectedUser.description))
                }
            }
        })
        binding.rvContent.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvContent.adapter = mAdapter

        //Refresh
        ViewUtil.initSwipeRefreshLayout(requireActivity(), binding.swipeRefreshLayout)
    }

    private fun observe() {
        registerObserverBaseEvent(viewModel, viewLifecycleOwner)
        viewModel.loadingEvent.observe(viewLifecycleOwner) { loadingEvent ->
            if (loadingEvent.isShow) {
                if (!binding.swipeRefreshLayout.isRefreshing) {
                    binding.statusLayout.showLoading()
                }
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
        binding.etKeyword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                filterList(editable.toString().trim())
            }
        })

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

    private fun filterList(query: String) {
        val filteredList = mList.filter { user ->
            user.username!!.contains(query, ignoreCase = true) || user.displayName!!.contains(query, ignoreCase = true)
        }

        mAdapter.updateData(filteredList)
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = true
        getData()
    }
}