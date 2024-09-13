package com.example.a912b.ui.home

import android.Manifest
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.a912b.R
import com.example.a912b.databinding.FragmentHomeBinding
import android.os.Vibrator
import androidx.annotation.RequiresApi

class HomeFragment : Fragment() {

    /*

                  VARIABLES

     */

    private var _binding: FragmentHomeBinding? = null

    // Esta propiedad solo es válida entre onCreateView y onDestroyView
    private val binding get() = _binding!!
    private lateinit var boton_llamado: Button


    //variables para el boton de alarma


    private lateinit var btn_alert: Button

    private lateinit var mediaPlayer: MediaPlayer

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var toggleRunnable: Runnable
    private var isToggling = false

    private lateinit var vibrator:Vibrator
    private lateinit var vibrationRunnable: Runnable
    private var isVibrating = false


    //variables para linterna


    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null
    private var isTorchOn = false
    private lateinit var boton_linterna: Button


    /*

                   METODOS

     */

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


    // Funciones del boton de alarma


    // Funcion para el sonido de la alarma
    private fun alarmSound(){
        if(mediaPlayer.isPlaying == false && mediaPlayer.isLooping == false){  // si mediaPlayer esta apagado y no esta loopeando lo enciende
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }else{ // en caso contratio lo pausa
            mediaPlayer.pause()
            mediaPlayer.isLooping = false
        }
    }

    // Funciones para loopear el flash

    private fun flashToggling(){
        if (isToggling) {  // si la linterna esta prendida la apaga
            handler.removeCallbacks(toggleRunnable)
            isToggling = false
            toggleFlashlight()
        } else { // si la linterna esta apagada la enciende llamando al hilo y setea isToggling en true
            handler.post(toggleRunnable)
            isToggling = true
        }
    }

    // Funcion para vibrar

    private fun vibratorLoop(){
        if (isVibrating) {  // si la vibracion ya esta encendida la apaga y setea isVibrating en false
            handler.removeCallbacks(vibrationRunnable)
            vibrator.cancel()
            isVibrating = false
        } else {  // si la vibracion esta apagada la enciende
            isVibrating = true
            handler.post(vibrationRunnable)
        }
    }




    /*

               METODOS PRINCIPALES

     */


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


        // Configuracion del boton de alarma


        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound) // crea el mediaPlayer
        mediaPlayer.setVolume(0.05F, 0.05F) // setea el volumen

        toggleRunnable = object : Runnable {   //crea el hilo para el flash
            override fun run() {
                toggleFlashlight()
                handler.postDelayed(this, 1000)
            }
        }

        vibrator = requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator // crea la variable vibrator con los servicios de vibracion
                                                                                    // solo esta deprecated a partir de la api 31

        vibrationRunnable = object : Runnable {  // crea el hilo para poder generar la vibracion
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                if (isVibrating) {   //se asegura que la vibracion esta en true
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(   //crea el efecto de vibracion con una duracion de 1 seg y una amplitud por defecto
                            1000,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                    handler.postDelayed(this, 3000)  // espera 3 segundos antes de volver a ejecutar el hilo
                }
            }
        }

        btn_alert = binding.btnAlerta  // crea el boton de alerta
        btn_alert.setOnClickListener{  // setea el listener del boton con sus respectivas funcionalidades
            alarmSound()
            flashToggling()
            vibratorLoop()
        }
    }

    // Limpia el binding cuando se destruye la vista
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer.stop()
        handler.removeCallbacks(toggleRunnable)
        isToggling = false
        handler.removeCallbacks(vibrationRunnable)
        vibrator.cancel()
        isVibrating = false
    }
}
