package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.myapplication.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<ViewModelType : BaseViewModel, VB : ViewBinding>(private val bindingInflater: (inflater: LayoutInflater) -> VB) :
    Fragment() {
    lateinit var viewModel: ViewModelType

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding as VB

    private fun initViewModel() {
        val parameterizedType = javaClass.genericSuperclass as? ParameterizedType
        val vmClass = parameterizedType?.actualTypeArguments?.getOrNull(0) as? Class<ViewModelType>?
        if (vmClass != null)
            viewModel = ViewModelProvider(this).get(vmClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater.invoke(layoutInflater)
        if (_binding == null)
            throw IllegalAccessException("binding cannot be null")
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}