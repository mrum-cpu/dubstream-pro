package com.dubstream.pro

import android.content.Context
import android.media.*
import android.os.Build

class AudioEngine(private val context: Context, private val onFrame: (ByteArray) -> Unit) {

    private val sampleRate = 16000
    private var recorder: AudioRecord? = null
    private var running = false

    fun start() {
        val bufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize * 2
        )

        recorder?.startRecording()
        running = true

        Thread {
            val buffer = ByteArray(bufferSize)
            while (running) {
                val read = recorder?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) {
                    onFrame(buffer.copyOf(read))
                }
            }
        }.start()
    }

    fun stop() {
        running = false
        recorder?.stop()
        recorder?.release()
        recorder = null
    }
}
