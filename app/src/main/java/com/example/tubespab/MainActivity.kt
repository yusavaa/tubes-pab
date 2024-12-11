package com.example.tubespab

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


/*
    Anggota:
    22523173 - Fath Yusava Arden
    22523295 - Alivah Syiva Dewi
    22523119 - Sekar Kinasih
    22523155 - Hasan Abdullah
 */

class MainActivity : AppCompatActivity() {

    private val TAG = "Firebase"
    private lateinit var mainText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mainText = findViewById(R.id.mainText)

        // Menghubungkan ke Firebase Realtime Database
        val database = FirebaseDatabase.getInstance("https://tubes-pab-kita-default-rtdb.asia-southeast1.firebasedatabase.app")
        val myRef = database.getReference("message")

        // Menulis data ke database
//        myRef.setValue("Hello Firebase!")

        // Membaca data dari database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Ambil data dari database
                val value = snapshot.getValue<String>()
                Log.d(TAG, "Value is: $value")
                mainText.text = value ?: "No data available"
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}
