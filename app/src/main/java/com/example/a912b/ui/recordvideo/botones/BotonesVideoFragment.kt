package com.example.a912b.ui.recordvideo.botones

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a912b.R
import com.example.a912b.databinding.FragmentBotonesVideoBinding
import com.example.a912b.ui.recordvideo.frontal.CamFrontalFragment
import com.example.a912b.ui.recordvideo.trasera.CamTraseraFragment

class BotonesVideoFragment : Fragment() {

    private var _binding : FragmentBotonesVideoBinding? = null
    private val binding get() = _binding!!
    companion object {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBotonesVideoBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val frontalBtn = binding.btnCamfrontal
        val traseraBtn = binding.btnCamtrasera

        frontalBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.child_fragment_container, CamFrontalFragment())
                .addToBackStack(null)
                .commit()
        }
        traseraBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.child_fragment_container, CamTraseraFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}