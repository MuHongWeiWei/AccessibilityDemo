package com.fly.accessibilitydemo.service

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.fly.accessibilitydemo.Common
import com.fly.accessibilitydemo.click
import com.fly.accessibilitydemo.input

class MyAccessibilityService : AccessibilityService() {

    private var autoTransferStatus = true
    private var login = false

    private val serviceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals(Common.AUTO_SERVICE_PACKAGE)) {
                performGlobalAction(GLOBAL_ACTION_HOME)

                Thread.sleep(1500)

                rootInActiveWindow?.findAccessibilityNodeInfosByText(intent.getStringExtra(Common.APP_NAME))
                    ?.apply {
                        if (size > 0) {
                            get(0).click()
                        }
                    }
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.packageName == Common.PACKAGE_NAME && event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && autoTransferStatus) {
            if(!login) {
                Thread.sleep(250)
                rootInActiveWindow?.findAccessibilityNodeInfosByViewId("${event.packageName}:id/etPhone")
                    ?.apply {
                        if (size > 0) {
                            get(0).input("0912345678")
                        }
                    }

                Thread.sleep(250)
                rootInActiveWindow?.findAccessibilityNodeInfosByViewId("${event.packageName}:id/btnNext")
                    ?.apply {
                        if (size > 0) {
                            get(0).click()
                        }
                    }

                Thread.sleep(250)
                rootInActiveWindow?.findAccessibilityNodeInfosByViewId("${event.packageName}:id/etPwd")
                    ?.apply {
                        if (size > 0) {
                            get(0).input("a123456")
                        }
                    }

                Thread.sleep(250)
                rootInActiveWindow?.findAccessibilityNodeInfosByViewId("${event.packageName}:id/btnNext")
                    ?.apply {
                        if (size > 0) {
                            get(0).click()
                        }
                    }
            }

            Thread.sleep(250)
            rootInActiveWindow?.findAccessibilityNodeInfosByText("天天來簽到")?.apply {
                if (size > 0) {
                    login = true
                    autoTransferStatus = false
                }
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        registerReceiver(serviceReceiver, IntentFilter(Common.AUTO_SERVICE_PACKAGE))
        Intent(Common.ACCESSIBLE_RECEIVER_ACTION).apply {
            putExtra(Common.ACCESSIBLE_STATUS, "開啟")
            sendBroadcast(this)
        }
    }

    override fun onInterrupt() {
        Intent(Common.ACCESSIBLE_RECEIVER_ACTION).apply {
            putExtra(Common.ACCESSIBLE_STATUS, "中斷")
            sendBroadcast(this)
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        unregisterReceiver(serviceReceiver)
        Intent(Common.ACCESSIBLE_RECEIVER_ACTION).apply {
            putExtra(Common.ACCESSIBLE_STATUS, "關閉")
            sendBroadcast(this)
        }
        return super.onUnbind(intent)
    }

}