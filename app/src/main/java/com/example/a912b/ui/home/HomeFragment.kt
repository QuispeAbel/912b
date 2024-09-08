package com.example.a912b.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.a912b.R
import com.example.a912b.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // Esta propiedad solo es válida entre onCreateView y onDestroyView
    private val binding get() = _binding!!
    private lateinit var boton_llamado: Button

    //variables para linterna
    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null
    private var isTorchOn = false
    private lateinit var boton_linterna: Button

    //funcion para encender y apagar la linterna
    private fun toggleFlashlight() {
        cameraId?.let {
            try {
                cameraManager.setTorchMode(it, !isTorchOn)
                isTorchOn = !isTorchOn
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido
        } else {
            // Permiso denegado
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }



    // Método para realizar la llamada
    private fun makePhoneCall() {
        val phoneNumber = "tel:113"
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse(phoneNumber)
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION)
        }
    }

    companion object {
        private const val REQUEST_CALL_PERMISSION = 1
    }

    // Infla la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura los componentes de la interfaz después de que la vista ha sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincula el botón y configura su listener
        boton_llamado = binding.btnEmergencia
        boton_llamado.setOnClickListener {
            makePhoneCall()
        }

        //inicializo camara manager
        cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList.firstOrNull { id ->
            cameraManager.getCameraCharacteristics(id).get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        }
        boton_linterna = binding.btnLinterna
        boton_linterna.setOnClickListener {
            toggleFlashlight()
        }
        checkPermissions()
    }

    // Limpia el binding cuando se destruye la vista
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
