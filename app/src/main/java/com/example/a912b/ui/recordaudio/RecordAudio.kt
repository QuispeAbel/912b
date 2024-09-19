package com.example.a912b.ui.recordaudio

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.a912b.databinding.FragmentRecordAudioBinding
import com.example.a912b.ui.notifications.NotificationsViewModel
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.widget.Button
import androidx.core.app.ActivityCompat

class RecordAudio : Fragment() {
    /*

    Variables

    */
    private var _binding: FragmentRecordAudioBinding? = null


    private val binding get() = _binding!!

    private val viewModel: RecordAudioViewModel by viewModels()

    // Variables del audio
    lateinit private var mr: MediaRecorder
    lateinit private var btPlay: Button
    lateinit private var btStop: Button
    lateinit private var btStart: Button

    lateinit var path: String

    /*

            Metodos Principales

    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentRecordAudioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        path = requireContext().getExternalFilesDir(null)?.absolutePath + "/myrec.3gp"
        mr = MediaRecorder()

        btPlay = binding.btnPlayRecord
        btPlay.isEnabled = false

        btStop = binding.btnStopRecord
        btStop.isEnabled = false

        btStart = binding.btnStartRecord
        btStart.isEnabled = false

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                111
            )
        } else {
            btPlay.isEnabled = true
            btStart.isEnabled = true
        }

        // Start Recording
        btStart.setOnClickListener {
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mr.setOutputFile(path)
            mr.prepare()
            mr.start()
            btStop.isEnabled = true
            btStart.isEnabled = false
        }

        // Stop Recording
        btStop.setOnClickListener {
            mr.stop()
            mr.reset()
            btStart.isEnabled = true
            btStop.isEnabled = false
            btPlay.isEnabled = true
        }

        // Play Recording
        btPlay.setOnClickListener {
            val mp = MediaPlayer()
            mp.setDataSource(path)
            mp.prepare()
            mp.start()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            btPlay.isEnabled = true
            btStart.isEnabled = true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}