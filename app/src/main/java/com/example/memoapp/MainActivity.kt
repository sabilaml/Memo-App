package com.example.memoapp

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memoapp.ui.theme.MemoAppTheme
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private val memoList = mutableStateListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)

        setContent {
            MemoAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    MemoApp()
                }
            }
        }
    }

    @Composable
    fun MemoApp() {
        var text by remember { mutableStateOf(TextFieldValue()) }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("App Memo", fontSize = 52.sp, color = Color.DarkGray, modifier = Modifier.padding(bottom = 16.dp))
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter Memo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Button(onClick = {
                if (memoList.size < 3) {
                    memoList.add(text.text)
                    text = TextFieldValue()
                }
            }, modifier = Modifier.padding(bottom = 16.dp)) {
                Text("Add Memo")
            }
            MemoList()
            Spacer(modifier = Modifier.weight(1f))
            Text("Developed by Sabila Marista Losya © 2024", color = Color.Gray, fontSize = 12.sp)
        }
    }

    @Composable
    fun MemoList() {
        Column {
            for ((index, memo) in memoList.withIndex()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(memo, modifier = Modifier.weight(1f), color = Color.Black)
                    Row {
                        IconButton(onClick = { speakOut(memo) }) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Speak Memo", tint = Color.Green)
                        }
                        IconButton(onClick = { memoList.removeAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Memo", tint = Color.Green)
                        }
                    }
                }
            }
        }
    }

    private fun speakOut(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.getDefault()
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}