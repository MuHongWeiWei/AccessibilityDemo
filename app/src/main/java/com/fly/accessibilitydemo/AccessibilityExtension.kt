package com.fly.accessibilitydemo

import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo

// 點擊
fun AccessibilityNodeInfo.click() = performAction(AccessibilityNodeInfo.ACTION_CLICK)

// 長按
fun AccessibilityNodeInfo.longClick() =
    performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)

// 向下滑動一下
fun AccessibilityNodeInfo.scrollForward() =
    performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)

// 向上滑動一下
fun AccessibilityNodeInfo.scrollBackward() =
    performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)

// 填充文字
fun AccessibilityNodeInfo.input(content: String) = performAction(
    AccessibilityNodeInfo.ACTION_SET_TEXT, Bundle().apply {
        putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, content)
    }
)