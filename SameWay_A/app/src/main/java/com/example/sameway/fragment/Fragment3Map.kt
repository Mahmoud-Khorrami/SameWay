package com.example.sameway.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sameway.databinding.Fragment3MapBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Fragment3Map : Fragment()
{
    lateinit var binding: Fragment3MapBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = Fragment3MapBinding.inflate(inflater,container,false)
        return binding.root
    }

}