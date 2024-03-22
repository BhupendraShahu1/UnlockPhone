package com.vikas.androidshell.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.view.ScrollingView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.vikas.androidshell.R
import com.vikas.androidshell.ViewModels.MainActivityViewModel
import com.vikas.androidshell.databinding.FragmentCommandBinding

class CommandFragment : Fragment() {

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var dataBinding: FragmentCommandBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_command, container, false)

        mainActivityViewModel.outputData.observe(viewLifecycleOwner, Observer {
            dataBinding.outPut.text = it
            dataBinding.scrollView.post {
                dataBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
        })

        return dataBinding.root
    }
}