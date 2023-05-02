package com.fly.accessibilitydemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val accessibleStatus = MutableLiveData<String>()
}
