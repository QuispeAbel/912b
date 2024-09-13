package com.example.a912b.ui.recordvideo

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.a912b.BotonesVideoFragment
import com.example.a912b.R
import com.example.a912b.databinding.FragmentHomeBinding
import com.example.a912b.databinding.FragmentRecordVideoBinding
import com.example.a912b.ui.recordvideo.frontal.CamFrontalFragment

class RecordVideoFragment : Fragment() {


    companion object {
        fun newInstance() = RecordVideoFragment()
    }

    private val viewModel: RecordVideoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_record_video, container, false)

        // Cargar un fragmento hijo inicial, por ejemplo, el FirstFragment
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.child_fragment_container, BotonesVideoFragment())
                .commit()
        }

        return view
    }

}