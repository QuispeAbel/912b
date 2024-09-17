package com.example.a912b.ui.recordvideo.frontal

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity.CAMERA_SERVICE
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.a912b.R
import com.example.a912b.databinding.FragmentCamFrontalBinding

class CamFrontalFragment : Fragment() {
    private var _binding : FragmentCamFrontalBinding? = null
    private val binding get() = _binding!!

    private lateinit var textureView: TextureView
    private lateinit var btnRecordSelfie: Button
    private lateinit var btnStopRecording: Button

    private var cameraDevice: CameraDevice? = null
    private var mediaRecorder: MediaRecorder? = null
    private var cameraCaptureSession: CameraCaptureSession? = null

    private lateinit var cameraManager: CameraManager
    private var cameraId: String = ""
    private var isRecording = false
    private lateinit var handler: Handler

    private val REQUEST_CAMERA_PERMISSION = 200

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCamFrontalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de componentes
        textureView = binding.textureView
        btnRecordSelfie = binding.btnGrabarSelfie
        btnStopRecording = binding.btnDetener

        cameraManager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager

        btnRecordSelfie.setOnClickListener {
            startRecording("1") // ID de la cámara frontal
        }

        btnStopRecording.setOnClickListener {
            stopRecording()
        }

        // Verificar permisos de la cámara y el audio
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
        }

        // Crear un hilo para las tareas en segundo plano
        val handlerThread = HandlerThread("CameraBackground")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

    }

    private fun startRecording(cameraId: String) {
        this.cameraId = cameraId
        setupMediaRecorder()
        openCamera()
        toggleButtons(true)
    }

    private fun stopRecording() {
        cameraCaptureSession?.stopRepeating()
        cameraCaptureSession?.abortCaptures()
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
        cameraDevice?.close()
        toggleButtons(false)
    }

    private fun openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {
                        cameraDevice = camera
                        startPreview(camera)
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        cameraDevice?.close()
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        cameraDevice?.close()
                        cameraDevice = null
                    }
                }, handler)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun startPreview(camera: CameraDevice) {
        val surfaceTexture = textureView.surfaceTexture!!
        surfaceTexture.setDefaultBufferSize(textureView.width, textureView.height)
        val previewSurface = Surface(surfaceTexture)
        val recordingSurface = mediaRecorder?.surface

        val captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
        captureRequestBuilder.addTarget(previewSurface)
        captureRequestBuilder.addTarget(recordingSurface!!)

        camera.createCaptureSession(listOf(previewSurface, recordingSurface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                cameraCaptureSession = session
                try {
                    session.setRepeatingRequest(captureRequestBuilder.build(), null, handler)
                    mediaRecorder?.start()
                    isRecording = true
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {}
        }, handler)
    }

    private fun setupMediaRecorder() {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "video_${System.currentTimeMillis()}.mp4")
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Movies/camarita") // Almacenamiento externo en carpeta "camarita"
        }

        val videoUri = requireActivity().contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(requireActivity().contentResolver.openFileDescriptor(videoUri!!, "w")?.fileDescriptor)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setVideoSize(1920, 1080)
            setVideoFrameRate(30)
            setOrientationHint(90)
            prepare()
        }
    }

    private fun toggleButtons(isRecording: Boolean) {
        btnRecordSelfie.isEnabled = !isRecording
        btnStopRecording.isEnabled = isRecording
    }
}