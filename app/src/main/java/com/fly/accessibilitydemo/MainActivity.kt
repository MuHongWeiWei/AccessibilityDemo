package com.fly.accessibilitydemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fly.accessibilitydemo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private val accessibilityPage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    // 監聽無障礙
    private val accessibleReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals(Common.ACCESSIBLE_RECEIVER_ACTION)) {
                mainViewModel.accessibleStatus.value =
                    intent.getStringExtra(Common.ACCESSIBLE_STATUS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerReceiver(accessibleReceiver, IntentFilter(Common.ACCESSIBLE_RECEIVER_ACTION))

        mainViewModel.accessibleStatus.observe(this) {
            binding.accessibleStatus.text = it
        }

        binding.fab.setOnClickListener {
            if (!isAccessibilityEnabled()) {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                accessibilityPage.launch(intent)
            } else {
                Intent(Common.AUTO_SERVICE_PACKAGE).apply {
                    putExtra(Common.APP_NAME, "麥當勞")
                    sendBroadcast(this)
                }
            }
        }
    }

    private fun isAccessibilityEnabled(): Boolean {
        val am = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        return am.isEnabled
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(accessibleReceiver)
    }

}