package com.ultivic.quickbrowser.fragments

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ultivic.quickbrowser.R
import com.ultivic.quickbrowser.base.BaseFragment
import com.ultivic.quickbrowser.databinding.FragmentHomeBinding
import com.ultivic.quickbrowser.vm.VM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "HomeFragment"

class HomeFragment : BaseFragment<FragmentHomeBinding, VM>() {

    val http = "https://"
    var isDelete = false
    var isExit = false
    var tabHistoryCount = 0

    private val args by navArgs<HomeFragmentArgs>()

    override fun getVM() = activityViewModels<VM>().value

    override fun getBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun bindUI(view: Context) {
        if (args.isUrlFromHistory)bindings.webView.loadUrl(args.historyUrl)

        Log.i(TAG, "bindUI: ${args}")
        bindings.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                bindings.progressBar.visibility = View.VISIBLE
                bindings.url.setText(url.toString())
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                bindings.progressBar.visibility = View.GONE

                val list = view?.copyBackForwardList()
                if (!isDelete) viewModel.insertHistory(list, args.tabData,bindings.root)


            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                Log.d(TAG, "shouldOverrideUrlLoading: ${request?.url}")

                isDelete = false
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        bindings.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                bindings.progressBar.progress = newProgress
                Log.d(TAG, newProgress.toString())
            }
        }
        bindings.webView.settings.javaScriptEnabled = true

        bindings.webView.settings.setSupportZoom(true)
        CoroutineScope(Dispatchers.IO).launch{
            delay(2000)
            withContext(Dispatchers.Main){
                if (!args.isUrlFromHistory) {
                    val d = bindings.url.text?.isEmpty()
                    if (d==true)bindings.webView.loadUrl("https://www.google.com/")
                }
            }
        }
    }

    override fun observer() {

        lifecycleScope.launch(Dispatchers.IO) {
            val d = viewModel.d(args.tabData.id)
            withContext(Dispatchers.Main){
                val url = if (d.lastUrl.contains("https://")) d.lastUrl else http + d.lastUrl
                if (!args.isUrlFromHistory)bindings.webView.loadUrl(url)
            }
        }

        viewModel.getTabsCount.observe(viewLifecycleOwner) {
            bindings.tabs.text = it.toString()
            Log.d(TAG, "observer: $it")
        }
        viewModel.historyCount(args.tabData.id).observe(viewLifecycleOwner) {
            tabHistoryCount = it
        }
    }

    override fun listener() {
        bindings.tabs.setOnClickListener {
            findNavController().navigate(R.id.allTabsFragment, bundleOf(Pair("isFromUser", true)))
        }
        bindings.url.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                isDelete = false
                val url = if (bindings.url.text.toString().contains("https://")) bindings.url.text.toString() else http + bindings.url.text.toString()
                bindings.webView.loadUrl(url)
                true
            } else false
        }

        bindings.tvHistory.setOnClickListener { findNavController().navigate(R.id.historyFragment) }
    }

    override fun onFragBack() {
        super.onFragBack()
        lifecycleScope.launch(Dispatchers.IO) {
            val d = viewModel.lastUrl(args.tabData.id)
            withContext(Dispatchers.Main) {
                if (tabHistoryCount > 0) {
                    isDelete = true
                    viewModel.deleteHistory(d,bindings.root)
                    val newUrl = d?.url ?: "https://www.google.com/"
                    bindings.webView.loadUrl(newUrl)
                } else {
                    if (isExit) activity?.finishAffinity() else {
                        bindings.webView.loadUrl("https://www.google.com/")
                        Toast.makeText(requireContext(), getString(R.string.Press_again), Toast.LENGTH_SHORT).show()
                        isExit = true
                    }
                }
            }
        }

    }

}