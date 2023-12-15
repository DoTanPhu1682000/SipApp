package com.dotanphu.sipapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.custom.ViewPager2Adapter
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.databinding.ActivityMainBinding
import com.dotanphu.sipapp.ui.contact.ContactFragment
import com.dotanphu.sipapp.ui.dialer.DialerFragment
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ViewPager2Adapter

    private val mOnItemSelectedListener = NavigationBarView.OnItemSelectedListener { item: MenuItem ->
        val itemId = item.itemId
        if (itemId == R.id.navRecently) {
            binding.viewPager.setCurrentItem(0, false)
            return@OnItemSelectedListener true
        } else if (itemId == R.id.navPhonebook) {
            binding.viewPager.setCurrentItem(1, false)
            return@OnItemSelectedListener true
        } else if (itemId == R.id.navKeyboard) {
            binding.viewPager.setCurrentItem(2, false)
            return@OnItemSelectedListener true
        } else if (itemId == R.id.navFavourite) {
            binding.viewPager.setCurrentItem(3, false)
            return@OnItemSelectedListener true
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        listener()
    }

    private fun initData() {
        binding.viewPager.isUserInputEnabled = false
        binding.navigation.setOnItemSelectedListener(mOnItemSelectedListener)

        binding.navigation.itemIconTintList = null

        //Chú ý: Không sử dụng ButterKnife trong các Fragment của ViewPager
        adapter = ViewPager2Adapter(this)

        adapter.addFragment(DialerFragment.newInstance())
        adapter.addFragment(ContactFragment.newInstance())
        adapter.addFragment(DialerFragment.newInstance())
        adapter.addFragment(DialerFragment.newInstance())
        binding.viewPager.adapter = adapter
    }

    private fun listener() {
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.navigation.selectedItemId = R.id.navRecently
                    1 -> binding.navigation.selectedItemId = R.id.navPhonebook
                    2 -> binding.navigation.selectedItemId = R.id.navKeyboard
                    3 -> binding.navigation.selectedItemId = R.id.navFavourite
                }
            }
        })
    }
}