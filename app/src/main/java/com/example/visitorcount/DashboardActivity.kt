package com.example.visitorcount

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

class DashboardActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val btnDownload: Button =findViewById(R.id.btnDownload)
        val db= FirebaseFirestore.getInstance()
        btnDownload.setOnClickListener{

//            db.collection("Entrada").get()
//                .addOnSuccessListener { entradaDocuments ->
//                    // Obtener los datos de la colección "Salida"
//                    db.collection("Salida").get()
//                        .addOnSuccessListener { salidaDocuments ->
//                            // Crear un nuevo libro de trabajo de Excel
//                            val csvFile = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "datos.csv")
//                            val csvWriter = FileWriter(csvFile)
//
//                            csvWriter.append("anio")
//                            csvWriter.append(",")
//                            csvWriter.append("mes")
//                            csvWriter.append(",")
//                            csvWriter.append("dia")
//                            csvWriter.append(",")
//                            csvWriter.append("hora")
//                            csvWriter.append(",")
//                            csvWriter.append("minuto")
//                            csvWriter.append(",")
//                            csvWriter.append("segundo")
//                            csvWriter.append(",")
//                            csvWriter.append("sexo")
//                            csvWriter.append("\n")
//
//                            // Recorrer los documentos de la colección "Entrada" y agregar los datos a Excel
//                            for (entradaDocument in entradaDocuments) {
//                                val rowData = entradaDocument.data
//                                csvWriter.append(rowData["anio"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["mes"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["dia"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["hora"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["minuto"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["segundo"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["sexo"].toString())
//                                csvWriter.append("\n")
//                            }
//                            for (salidaDocument  in salidaDocuments) {
//                                val rowData = salidaDocument.data
//                                csvWriter.append(rowData["anio"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["mes"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["dia"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["hora"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["minuto"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["segundo"].toString())
//                                csvWriter.append(",")
//                                csvWriter.append(rowData["sexo"].toString())
//                                csvWriter.append("\n")
//                            }
//                            csvWriter.flush()
//                            csvWriter.close()
//
//                            Toast.makeText(
//                                this,
//                                "Archivo Excel descargado",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                        .addOnFailureListener { exception ->
//                            Toast.makeText(
//                                this,
//                                "Error al obtener los datos de la colección 'Salida'",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                }
//                .addOnFailureListener { exception ->
//                    Toast.makeText(
//                        this,
//                        "Error al obtener los datos de la colección 'Entrada'",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
            if (isStoragePermissionGranted()) {
                downloadDataAsCSV(db)
            } else {
                // Request the required permissions
                requestStoragePermission()
            }
        }
    }
    private fun isStoragePermissionGranted(): Boolean {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permissionResult = ContextCompat.checkSelfPermission(this, permission)
        return permissionResult == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(this, arrayOf(permission), STORAGE_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initiate the download process
                val db = FirebaseFirestore.getInstance()
                downloadDataAsCSV(db)
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied. Unable to download data.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun downloadDataAsCSV(db: FirebaseFirestore) {
        db.collection("Entrada").get()
            .addOnSuccessListener { entradaDocuments ->
                // Obtener los datos de la colección "Salida"
                db.collection("Salida").get()
                    .addOnSuccessListener { salidaDocuments ->
                        // Crear un nuevo libro de trabajo de Excel
                        val csvFile = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "datos.csv")
                        val csvWriter = FileWriter(csvFile)

                        csvWriter.append("anio")
                        csvWriter.append(",")
                        csvWriter.append("mes")
                        csvWriter.append(",")
                        csvWriter.append("dia")
                        csvWriter.append(",")
                        csvWriter.append("hora")
                        csvWriter.append(",")
                        csvWriter.append("minuto")
                        csvWriter.append(",")
                        csvWriter.append("segundo")
                        csvWriter.append(",")
                        csvWriter.append("sexo")
                        csvWriter.append("\n")

                        // Recorrer los documentos de la colección "Entrada" y agregar los datos a Excel
                        for (entradaDocument in entradaDocuments) {
                            val rowData = entradaDocument.data
                            csvWriter.append(rowData["anio"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["mes"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["dia"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["hora"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["minuto"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["segundo"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["sexo"].toString())
                            csvWriter.append("\n")
                        }
                        for (salidaDocument  in salidaDocuments) {
                            val rowData = salidaDocument.data
                            csvWriter.append(rowData["anio"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["mes"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["dia"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["hora"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["minuto"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["segundo"].toString())
                            csvWriter.append(",")
                            csvWriter.append(rowData["sexo"].toString())
                            csvWriter.append("\n")
                        }
                        csvWriter.flush()
                        csvWriter.close()

                        Toast.makeText(
                            this,
                            "Archivo Excel descargado",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this,
                            "Error al obtener los datos de la colección 'Salida'",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error al obtener los datos de la colección 'Entrada'",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}