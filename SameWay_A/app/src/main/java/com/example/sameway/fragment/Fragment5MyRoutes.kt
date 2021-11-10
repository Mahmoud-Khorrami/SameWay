package com.example.sameway.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sameway.databinding.Fragment5MyRouteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Fragment5MyRoutes : Fragment()
{

    lateinit var binding: Fragment5MyRouteBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = Fragment5MyRouteBinding.inflate(inflater,container,false)
        return binding.root
    }
}