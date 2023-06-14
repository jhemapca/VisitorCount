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
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Align
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.LegendLayout
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileWriter

class DashboardActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_REQUEST_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val btnDownload: Button =findViewById(R.id.btnDownload)
        val db= FirebaseFirestore.getInstance()
        val grafico:AnyChartView = findViewById(R.id.any_chart_view)
        val pie:Pie=AnyChart.pie()
        val grafico2:AnyChartView=findViewById(R.id.bar_chart_view)
        val cartesian=AnyChart.column()
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
        }
        val dataPie:ArrayList<DataEntry> = ArrayList()
        val dataBarHE:ArrayList<DataEntry> = ArrayList()
        val horas:Array<String> = arrayOf("0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23")
        var contHE:MutableList<Int> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

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
                        for (i in 0..23){
                            if(dc.document.data["hora"].toString()==horas[i]){
                                contHE[i]+=1
                            }
                        }
                    }
                }
            }
            for(i in 0..23){
                dataBarHE.add(ValueDataEntry(horas[i],contHE[i]))
            }
            dataPie.add(ValueDataEntry("Hombres",h))
            dataPie.add(ValueDataEntry("Mujeres",m))
            pie.data(dataPie)
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

        var column: Column = cartesian.column(dataBarHE)
        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("${"%"}{groupsSeparator: }")
        cartesian.animation(true)
        cartesian.title("Entrada de hombres por hora")
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)
        cartesian.xAxis(0).title("Horas")
        cartesian.yAxis(0).title("N°Personas")
        grafico2.setChart(cartesian)

    }
}