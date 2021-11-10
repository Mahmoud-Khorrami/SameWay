package com.example.sameway.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sameway.databinding.Fragment4SameWayBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Fragment4SameWay : Fragment()
{
    lateinit var binding: Fragment4SameWayBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = Fragment4SameWayBinding.inflate(inflater,container,false)
        return binding.root
    }

}