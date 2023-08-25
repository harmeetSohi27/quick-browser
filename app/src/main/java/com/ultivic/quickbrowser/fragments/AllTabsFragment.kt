package com.ultivic.quickbrowser.fragments

import android.content.Context
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ultivic.quickbrowser.R
import com.ultivic.quickbrowser.adapters.TabsAdapter
import com.ultivic.quickbrowser.base.BaseFragment
import com.ultivic.quickbrowser.databinding.FragmentAllTabsBinding
import com.ultivic.quickbrowser.vm.VM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllTabsFragment : BaseFragment<FragmentAllTabsBinding, VM>() {
    private val adapter by lazy { TabsAdapter() }

    private val nav by navArgs<AllTabsFragmentArgs>()

    override fun getVM() = activityViewModels<VM>().value
    override fun getBinding() = FragmentAllTabsBinding.inflate(layoutInflater)

    override fun bindUI(view: Context) {
        if (!nav.isFromUser) lifecycleScope.launch(Dispatchers.IO) {
            val d = viewModel.lastTab
            if (d != null) {
                withContext(Dispatchers.Main) {
                    findNavController().navigate(R.id.homeFragment, bundleOf(Pair("tabData", d)))
                }
            } else {
//                viewModel.insertDefault
            }
        }

        bindings.recyclerView.adapter = adapter
    }

    override fun observer() {
        viewModel.tabsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
         }
        viewModel.getTabsCount.observe(viewLifecycleOwner) {
            Log.d("observer: ", "$it")
            if (it == 0) viewModel.insertDefault
        }

        viewModel.newTab.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.homeFragment, bundleOf(Pair("tabData", it)))
                viewModel.newTab.value = null
            }
        }
    }

    override fun listener() {
        with(bindings) {
            adapter.onDeleteTab = {
                viewModel.delete = it.id
                viewModel.deleteTabHistory = it.id
            }

            newTab.setOnClickListener {
                viewModel.insertDefault
            }

            adapter.onClick = {
                findNavController().navigate(R.id.homeFragment, bundleOf(Pair("tabData", it)))
            }
        }
    }

    override fun onFragBack() {
        super.onFragBack()
        findNavController().popBackStack()
    }
}