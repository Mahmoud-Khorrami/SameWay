package com.example.sameway.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sameway.databinding.Fragment1RegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Fragment0SplashScreen : Fragment()
{
    lateinit var binding: Fragment1RegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = Fragment1RegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

}