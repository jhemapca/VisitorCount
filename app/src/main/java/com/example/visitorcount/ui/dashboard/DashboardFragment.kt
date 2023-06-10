package com.example.visitorcount.ui.dashboard
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.visitorcount.R
import com.example.visitorcount.databinding.FragmentDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val btnH: Button = binding.regInBtnH
        val btnM: Button = binding.regInBtnM
        val db = FirebaseFirestore.getInstance()
        btnH.setOnClickListener{
            // Obtener el tiempo actual
            val tiempoActual = LocalDateTime.now()
            val datos = hashMapOf(
                "sexo" to "Hombre",
                "anio" to tiempoActual.year,
                "mes" to tiempoActual.monthValue,
                "dia" to tiempoActual.dayOfMonth,
                "hora" to tiempoActual.hour,
                "minuto" to tiempoActual.minute,
                "segundo" to tiempoActual.second
            )
            val newDocRef=db.collection("Entrada").document()
            newDocRef.set(datos)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Registro guardado con ID: ${newDocRef.id}")
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Error al guardar el registro", e)
                }
        }
        btnM.setOnClickListener{
            // Obtener el tiempo actual
            val tiempoActual = LocalDateTime.now()
            val datos = hashMapOf(
                "sexo" to "Mujer",
                "anio" to tiempoActual.year,
                "mes" to tiempoActual.monthValue,
                "dia" to tiempoActual.dayOfMonth,
                "hora" to tiempoActual.hour,
                "minuto" to tiempoActual.minute,
                "segundo" to tiempoActual.second
            )
            val newDocRef=db.collection("Entrada").document()
            newDocRef.set(datos)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Registro guardado con ID: ${newDocRef.id}")
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Error al guardar el registro", e)
                }
        }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}