package com.moham.digssquared.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moham.digssquared.data.entities.Values
import com.moham.digssquared.data.repository.Repository
import com.moham.digssquared.utils.Resource
import com.moham.digssquared.utils.Resource.Status.SUCCESS
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _values = MutableLiveData<Resource<Values?>>()
    val values: LiveData<Resource<Values?>> = _values


    fun getValues() {
        viewModelScope.launch {
            _values.value = Resource.loading()
           val response =  repository.getValues()
            if (response.status == SUCCESS){
                _values.value = Resource.success(response.data)
            }else{
                _values.value = Resource.error(response.message)
            }
        }
    }

}
