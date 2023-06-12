package com.example.visitorcount

import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileWriter

class DashboardActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val btnDownload: Button =findViewById(R.id.btnDownload)
        val db= FirebaseFirestore.getInstance()
        val grafico:AnyChartView = findViewById(R.id.any_chart_view)
        val pie:Pie=AnyChart.pie()
        btnDownload.setOnClickListener{
            db.collection("Entrada").addSnapshotListener{snapshots,e->

                if (e!=null){
                    return@addSnapshotListener
                }
                val csvFile = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "datos.csv")
                val csvWriter = FileWriter(csvFile)
                csvWriter.append("anio,mes,dia,hora,minuto,segundo,sexo\n")

                for (dc in snapshots!!.documentChanges){
                    when(dc.type){
                        DocumentChange.Type.ADDED,
                        DocumentChange.Type.MODIFIED,
                        DocumentChange.Type.REMOVED->{
                            val rowData = dc.document.data
                            csvWriter.append("${rowData["anio"]},${rowData["mes"]},${rowData["dia"]},${rowData["hora"]},${rowData["minuto"]},${rowData["segundo"]},${rowData["sexo"]}\n")
                        }
                    }
                }
                csvWriter.flush()
                csvWriter.close()

                Toast.makeText(
                    this,
                    "Archivo Excel descargado",
                    Toast.LENGTH_LONG).show()
            }

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
        }
        val data:ArrayList<DataEntry> = ArrayList()

        db.collection("Entrada").addSnapshotListener{snapshots,e->
            if (e!=null){
                return@addSnapshotListener
            }
            var h:Int=0
            var m:Int=0
            for (dc in snapshots!!.documentChanges){
                when(dc.type){
                    DocumentChange.Type.ADDED,
                    DocumentChange.Type.MODIFIED,
                    DocumentChange.Type.REMOVED->{
                        if(dc.document.data["sexo"].toString()=="Hombre"){
                            h += 1
                        }else{
                            m += 1
                        }
                    }
                }
            }
            data.add(ValueDataEntry("Hombres",h))
            data.add(ValueDataEntry("Mujeres",m))
            pie.data(data)
        }



        pie.title("Cantidad de asistentes según género")
        pie.labels().position("outside")
        pie.legend().title().enabled(true)
        pie.legend().title()
            .text("Género")
            .padding(0.0, 0.0, 10.0, 0.0)
        pie.legend()
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(Align.CENTER);

        grafico.setChart(pie)

    }
}