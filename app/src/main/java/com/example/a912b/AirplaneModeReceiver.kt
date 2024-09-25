package com.example.a912b

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AirplaneModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = intent.getBooleanExtra("state", false)
            if (isAirplaneModeOn) {
                // Mostrar ventana emergente
                showAirplaneModeDialog(context)
            }
        }
    }

    private fun showAirplaneModeDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Modo avión activado")
        builder.setMessage("El modo avión está activado. Algunas funciones no estarán disponibles:\n" +
                "- Llamadas\n" +
                "- La ubicación no será correcta\n" +
                "- Los mensajes se enviarán cuando se vuelva a conectar")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }
}

