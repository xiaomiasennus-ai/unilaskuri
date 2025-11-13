package com.example.unilaskuri

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unilaskuri.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: SleepDatabase
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = SleepDatabase.getDatabase(this)

        binding.calculateButton.setOnClickListener {
            calculateAndSave()
        }

        loadHistory()
    }

    private fun calculateAndSave() {
        try {
            val start1 = dateFormat.parse(binding.sleep1Start.text.toString())
            val end1   = dateFormat.parse(binding.sleep1End.text.toString())
            val start2 = dateFormat.parse(binding.sleep2Start.text.toString())
            val end2   = dateFormat.parse(binding.sleep2End.text.toString())

            val mins1 = if (end1.before(start1)) {
                (end1.time + 24*60*60*1000 - start1.time) / 60000
            } else (end1.time - start1.time) / 60000

            val mins2 = if (end2.before(start2)) {
                (end2.time + 24*60*60*1000 - start2.time) / 60000
            } else (end2.time - start2.time) / 60000

            val total = mins1 + mins2
            val hours = total / 60
            val mins  = total % 60

            binding.resultText.text = "Yhteensä nukuttu: $hours tuntia ja $mins minuuttia"

            CoroutineScope(Dispatchers.IO).launch {
                db.sleepDao().insert(
                    SleepSession(
                        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                        totalMinutes = total.toInt()
                    )
                )
                withContext(Dispatchers.Main) { loadHistory() }
            }
        } catch (e: Exception) {
            binding.resultText.text = "Tarkista syötteet (HH:mm)"
        }
    }

    private fun loadHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = db.sleepDao().getAll()
            withContext(Dispatchers.Main) {
                findViewById<RecyclerView>(R.id.historyList).apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = HistoryAdapter(list)
                }
            }
        }
    }
}
