package com.ultivic.quickbrowser.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ultivic.quickbrowser.R
import com.ultivic.quickbrowser.adapters.HistoryAdapter
import com.ultivic.quickbrowser.base.BaseFragment
import com.ultivic.quickbrowser.databinding.FragmentHistoryBinding
import com.ultivic.quickbrowser.models.TabHistory
import com.ultivic.quickbrowser.vm.VM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HistoryFragment : BaseFragment<FragmentHistoryBinding, VM>() {
    private val adapter by lazy { HistoryAdapter() }
    override fun getVM() = activityViewModels<VM>().value

    override fun getBinding() = FragmentHistoryBinding.inflate(layoutInflater)

    override fun observer() {
        viewModel.historyList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun bindUI(view: Context) {
        bindings.rvHistory.adapter = adapter
    }

    override fun listener() {
        bindings.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        bindings.btnDelete.setOnClickListener {
            viewModel.deleteAll()
        }
        adapter.onClick = {
            getTAb(it)
        }
        adapter.onLongClick = {
            val clipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Copied Text", it.url)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(requireContext(), getString(R.string.url_copied), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTAb(tabHistory: TabHistory) {
        lifecycleScope.launch(Dispatchers.IO) {
            val d = viewModel.d(tabHistory.tabId)
            withContext(Dispatchers.Main) {
                findNavController().navigate(R.id.homeFragment, bundleOf(Pair("tabData", d),Pair("historyUrl",tabHistory.url), Pair("isUrlFromHistory",true)))
            }
        }
    }

    override fun onFragBack() {
        super.onFragBack()
        findNavController().popBackStack()
    }
}