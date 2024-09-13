package com.example.a912b.ui.recordvideo.trasera

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a912b.R

class CamTraseraFragment : Fragment() {

    companion object {
        fun newInstance() = CamTraseraFragment()
    }

    private val viewModel: CamTraseraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_cam_trasera, container, false)
    }
}