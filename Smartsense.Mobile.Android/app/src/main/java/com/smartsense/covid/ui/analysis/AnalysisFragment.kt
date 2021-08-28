package com.smartsense.covid.ui.analysis

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartsense.covid.R
import android.widget.Chronometer

import android.media.MediaRecorder

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri

import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AnalysisFragment : Fragment() {

    private lateinit var viewModel: AnalysisViewModel

    private lateinit var recordButton: ImageButton
    private lateinit var timer: Chronometer
    private lateinit var playVoice: ImageView
    private lateinit var sendToAnalysis: MaterialButton
    private lateinit var playSendLayout: ConstraintLayout

    private var isRecording = false

    private val recordPermission: String = Manifest.permission.RECORD_AUDIO
    private val PERMISSION_CODE = 21

    private lateinit var mediaRecorder: MediaRecorder
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    private var recordFile: String? = null
    private var recordFilePath: String = ""
    private lateinit var snackbar: Snackbar

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AnalysisViewModel::class.java)

        recordButton = view.findViewById(R.id.recordButton)
        timer = view.findViewById(R.id.recordTimer)
        playVoice = view.findViewById(R.id.playButton)
        sendToAnalysis = view.findViewById(R.id.sendToAnalysis)
        playSendLayout = view.findViewById(R.id.playSendLayout)



        recordButton.setOnClickListener {
            if (isRecording) {
                //Stop Recording
                stopRecording()
            } else {
                //Check permission to record audio
                if (checkPermissions()) {
                    //Start Recording
                    startRecording()
                }
            }
        }


        timer.setOnChronometerTickListener {
            if ((SystemClock.elapsedRealtime() - it.base) >= 30000) {
                stopRecording()
            }
        }

        sendToAnalysis.setOnClickListener {

        }

        playVoice.setOnClickListener {
           if(!isPlaying){
               playAudio(File(recordFilePath))
           }else{
               stopAudio()
           }
        }
    }


    private fun stopRecording() {
        //Stop Timer, very obvious
        timer.stop()

        //Stop media recorder and set it to null for further use to record new audio
        mediaRecorder.stop()
        mediaRecorder.release()
        // mediaRecorder = null

        // Change button image and set Recording state to false
        recordButton.setImageDrawable(
                ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.record_btn_stopped
                )
        )
        isRecording = false

        playSendLayout.visibility = View.VISIBLE
    }

    private fun startRecording() {
        //Start timer from 0
        timer.base = SystemClock.elapsedRealtime()
        timer.start()

        //Get app external directory path
        val recordPath = requireActivity().getExternalFilesDir("/")!!.absolutePath

        //Get current date and time
        val formatter = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.ROOT)
        val now = Date()

        //initialize filename variable with date and time at the end to ensure the new file wont overwrite previous file
        recordFile = "Recording_" + formatter.format(now).toString() + ".m4a"

        //Setup Media Recorder for recording
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setOutputFile("$recordPath/$recordFile")
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder.setAudioEncodingBitRate(16*44100)
        mediaRecorder.setAudioSamplingRate(44100)

        recordFilePath = "$recordPath/$recordFile"

        try {
            mediaRecorder.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //Start Recording
        mediaRecorder.start()

        // Change button image and set Recording state to false
        recordButton.setImageDrawable(
                ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.record_btn_recording
                )
        )
        isRecording = true

        playSendLayout.visibility = View.GONE
    }

    private fun playAudio(fileToPlay: File) {
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer!!.setDataSource(fileToPlay.absolutePath)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()

        playVoice.setImageDrawable(
                ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.player_pause_btn
                )
        )

        mediaPlayer!!.setOnCompletionListener {
            stopAudio()
        }

        isPlaying = true
    }

    private fun stopAudio() {
        if(mediaPlayer !== null){
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
        }

        playVoice.setImageDrawable(
                ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.player_play_btn
                )
        )

        isPlaying = false
    }

    private fun checkPermissions(): Boolean {
        //Check permission
        return if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        recordPermission
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            //Permission Granted
            true
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(recordPermission),
                    PERMISSION_CODE
            )
            false
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (snackbar.isShown) {
                    snackbar.dismiss()
                }
                startRecording()
            } else {
                snackbar = Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.record_permission), Snackbar.LENGTH_INDEFINITE
                )
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.setAction(getString(R.string.give_permission)) {
                    val i = Intent()
                    i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    i.data = Uri.parse("package:" + requireActivity().packageName)
                    i.addCategory(Intent.CATEGORY_DEFAULT)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                    startActivity(i)
                }.show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (isRecording) {
            stopRecording()
        }
    }

}