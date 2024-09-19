package com.example.a912b

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class adaptermensajes(private val mensajesList: MutableList<mensaje>) : RecyclerView.Adapter<adaptermensajes.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombre_chat)
        val mensaje: TextView = itemView.findViewById(R.id.mensaje_chat)
        val hora: TextView = itemView.findViewById(R.id.hora_chat)
        //val fotoperfil: ImageView = itemView.findViewById(R.id.foto_perfil_chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_mensajes, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mensaje = mensajesList[position]
        holder.nombre.text = mensaje.nombre
        holder.mensaje.text = mensaje.mensaje
        holder.hora.text = mensaje.hora
        // Aquí puedes cargar la imagen en fotoperfil si es necesario
    }



    override fun getItemCount(): Int = mensajesList.size

    fun addMensaje(mensaje: mensaje) {
        mensajesList.add(mensaje)
        notifyItemInserted(mensajesList.size - 1)
    }
}



/*data class Mensaje(val nombre: String, val texto: String, val hora: String)

// Crea nuevas vistas (invocado por el layout manager)
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMensajes {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_main, parent, false)
    return HolderMensajes(view)
}

 // Reemplaza el contenido de una vista (invocado por el layout manager)
    override fun onBindViewHolder(holder: HolderMensajes, position: Int) {
        val mensajesList: List<Mensaje> = listOf() // Ejemplo de inicialización
        holder.nombre.text = mensaje.nombre
        holder.mensaje.text = mensaje.texto
        holder.hora.text = mensaje.hora
        // Aquí puedes cargar la imagen en fotoperfil si es necesario
    }*/