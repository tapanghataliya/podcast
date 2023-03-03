package com.example.podcastapp.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel<*>> : Fragment() {

    private var baseActivity: BaseActivity? = null
    private var rootView: View? = null
    private var viewDataBinding: V? = null

    protected lateinit var viewModel: VM

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingClass(): V

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (requireContext() is BaseActivity) {
            baseActivity = requireContext() as BaseActivity

        }
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        rootView = viewDataBinding?.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.setVariable(getBindingVariable(), viewModel)
    }

    fun getBaseActivity(): BaseActivity? = baseActivity

    fun getViewDataBinding(): ViewDataBinding? = viewDataBinding

    /**
     * Check if device is connected to internet
     */
    fun isNetworkConnected(): Boolean {
        return false
    }

    /**
     * Hide soft keyboard
     */
    fun hideKeyboard() {
        baseActivity?.hideKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }
}