package com.smartsense.covid.ui.analysis

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.smartsense.covid.PrefManager
import com.smartsense.covid.R
import com.smartsense.covid.api.ApiConstant
import com.smartsense.covid.api.ApiConstantText
import com.smartsense.covid.api.model.requests.AudioToAnalysisRequest
import com.smartsense.covid.api.model.responses.AudioToAnalysisResponse
import com.smartsense.covid.api.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class AnalysisFragment : Fragment() {

    private var TAG = "Analysis"
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

    private lateinit var apiText: ApiConstantText
    private lateinit var prefManager: PrefManager
    private lateinit var loadingDialog: Dialog
    private lateinit var resultDialog: Dialog
    private lateinit var resultText: TextView

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


        prefManager = PrefManager(context)
        apiText = ApiConstantText(context)

        loadingDialog = Dialog(requireContext())
        loadingDialog.setContentView(R.layout.dialog_loading_text)
        loadingDialog.setCancelable(false)
        val dm = resources.displayMetrics
        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog.window?.setLayout(dm.widthPixels - 100, LinearLayout.LayoutParams.WRAP_CONTENT)

        resultDialog = Dialog(requireContext())
        resultDialog.setContentView(R.layout.dialog_analysis_result)
        resultDialog.setCancelable(true)
        resultDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        resultDialog.window?.setLayout(dm.widthPixels - 100, LinearLayout.LayoutParams.WRAP_CONTENT)
        resultText = resultDialog.findViewById(R.id.analysisResultText)

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
            if (!loadingDialog.isShowing) {
                loadingDialog.show()
            }

            val handler = Handler()
            val runnable = Runnable {
                val fileInputStream: InputStream = FileInputStream(File(recordFilePath))
                val bytesData = convertStreamToByteArray(fileInputStream)
                val request = AudioToAnalysisRequest()
                if (bytesData != null) {
                    request.audio = bytesData
                    audioToAnalysis(request)
                }
            }
            handler.postDelayed(runnable, 100)
        }

        playVoice.setOnClickListener {
           if(!isPlaying){
               playAudio(File(recordFilePath))
           }else{
               stopAudio()
           }
        }
    }

    private fun audioToAnalysis(request: AudioToAnalysisRequest) {
        val call = RetrofitClient.getInstance()
            .getApi(context).audioToAnalysis(request)
        call.enqueue(object : Callback<AudioToAnalysisResponse?> {
            override fun onResponse(
                call: Call<AudioToAnalysisResponse?>,
                response: Response<AudioToAnalysisResponse?>
            ) {
                if (response.code() == 200) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            if (response.body()!!.code == "200") {
                                Log.i("Analysis", "onResponse: code 200")
                                if (loadingDialog.isShowing) {
                                    loadingDialog.dismiss()
                                }

                                if(response.body()!!.isCovid){
                                    resultText.text = getString(R.string.analysis_result_covid)
                                    if (!resultDialog.isShowing) {
                                        resultDialog.show()
                                    }
                                }else{
                                    resultText.text = getString(R.string.analysis_result_not_covid)
                                    if (!resultDialog.isShowing) {
                                        resultDialog.show()
                                    }
                                }

                            } else if (response.body()!!.code == "400") {
                                val errors = StringBuilder()
                                for (i in response.body()!!.errors.indices) {
                                    errors.append(response.body()!!.errors[response.body()!!.errors.size - 1 - i])
                                    errors.append("\n")
                                }
                                getLongToast(errors.toString())
                            }
                        } else {
                            getShortToast(getString(R.string.occurred_error) + " 481")
                        }
                    } else {
                        getShortToast(getString(R.string.occurred_error) + " 482")
                    }
                } else {
                    try {
                        when {
                            response.code() == ApiConstant.BAD_REQUEST -> {
                                getShortToast(apiText.getText(ApiConstant.BAD_REQUEST))
                            }
                            response.code() == ApiConstant.UNAUTHORIZED -> {
                                getShortToast(apiText.getText(ApiConstant.UNAUTHORIZED))
                            }
                            response.code() == ApiConstant.FORBIDDEN -> {
                                getShortToast(apiText.getText(ApiConstant.FORBIDDEN))
                            }
                            response.code() == ApiConstant.INTERNAL_SERVER -> {
                                getShortToast(apiText.getText(ApiConstant.INTERNAL_SERVER))
                            }
                            else -> {
                                getShortToast(getString(R.string.occurred_error) + " 483")
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        getShortToast(getString(R.string.occurred_error) + " 484")
                    }
                }
                if (loadingDialog.isShowing) {
                    loadingDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<AudioToAnalysisResponse?>, t: Throwable) {
                Log.i(TAG, "onFailure: "+t.message)
                Toast.makeText(
                    context,
                    getString(R.string.occurred_error) + " 485",
                    Toast.LENGTH_SHORT
                ).show()
                if (loadingDialog.isShowing) {
                    loadingDialog.dismiss()
                }
            }
        })
    }

    private fun getShortToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun getLongToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
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


    fun convertStreamToByteArray(inputStream: InputStream): ByteArray? {
        val baos = ByteArrayOutputStream()
        val buff = ByteArray(10240)
        var i = Int.MAX_VALUE
        while (inputStream.read(buff, 0, buff.size).also { i = it } > 0) {
            baos.write(buff, 0, i)
        }
        return baos.toByteArray() // be sure to close InputStream in calling function
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