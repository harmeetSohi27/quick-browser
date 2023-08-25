package com.ultivic.quickbrowser.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.ultivic.quickbrowser.utils.onBack

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>() : Fragment() {

    private lateinit var _vm: VM
    private lateinit var _vb: VB

    val viewModel get() = _vm


    val bindings get() = _vb

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _vb = getBinding()
        _vm = getVM()
        return _vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI(view.context)
        listener()
        observer()
        onBack {
            onFragBack()
        }
    }

    open fun onFragBack() {

    }

    abstract fun observer()

    abstract fun bindUI(view: Context)

    abstract fun getVM(): VM

    abstract fun getBinding(): VB

    abstract fun listener()
}