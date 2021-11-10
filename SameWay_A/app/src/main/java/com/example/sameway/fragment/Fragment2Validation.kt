package com.example.sameway.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sameway.databinding.Fragment2ValidationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Fragment2Validation : Fragment()
{
    lateinit var binding: Fragment2ValidationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = Fragment2ValidationBinding.inflate(inflater,container,false)
        return binding.root
    }

}