package com.example.a912b.ui.notifications

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a912b.adaptermensajes
import com.example.a912b.databinding.FragmentNotificationsBinding
import com.example.a912b.mensaje
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class NotificationsFragment : Fragment() {

    private var _binding : FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var nombre : String
    private lateinit var vistarecycle : RecyclerView
    private lateinit var txtmensaje : EditText
    private lateinit var btnenviar : Button

    private lateinit var adapter : adaptermensajes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nombre = "Pepe"
        vistarecycle = binding.rvmensajes
        txtmensaje = binding.txtMensaje
        btnenviar = binding.btnEnviar

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("chat")

        adapter = adaptermensajes(mutableListOf())
        vistarecycle.layoutManager = LinearLayoutManager(requireContext())
        vistarecycle.adapter = adapter

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = sdf.format(Calendar.getInstance().time)

        btnenviar.setOnClickListener {
            val mensaje = mensaje(
                mensaje = txtmensaje.text.toString(),
                nombre = nombre,
                hora = currentTime // Usa la hora actual
            )
            databaseReference.push().setValue(mensaje)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Mensaje enviado correctamente")
                    } else {
                        Log.e(TAG, "Error al enviar mensaje", task.exception)
                    }
                }
            txtmensaje.setText("")
        }
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                setScrollbar()
            }
        })

        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val m: mensaje? = dataSnapshot.getValue(mensaje::class.java)
                if (m != null) {
                    adapter.addMensaje(m)
                }
            }


            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Tu lógica aquí
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                // Tu lógica aquí
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Tu lógica aquí
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.e(TAG, "DatabaseError: ${databaseError.message}")
            }
        })

    }
    private fun setScrollbar() {
        vistarecycle.scrollToPosition(adapter.itemCount - 1)
    }

    fun add(mensaje: mensaje) {
        adapter.addMensaje(mensaje)
    }
}




