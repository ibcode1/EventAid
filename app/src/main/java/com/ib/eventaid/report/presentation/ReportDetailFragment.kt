package com.ib.eventaid.report.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.ib.eventaid.databinding.FragmentReportDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.net.URL
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class ReportDetailFragment : Fragment() {

    companion object {
        private const val API_URL = "https://example.com/?send_report"
        private const val REPORT_APP_ID = 46341L
        private const val REPORT_PROVIDER_ID = 46341L
        private const val REPORT_SESSION_KEY = "session_key_in_next_chapter"
    }

    object ReportTracker {
        var reportNumber = AtomicInteger()
    }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                showUploadedFile(uri)
            }
        }

    @Volatile
    private var isSendingReport = false

    private val binding get() = _binding!!
    private var _binding: FragmentReportDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportDetailBinding.inflate(inflater, container, false)

        binding.sendButton.setOnClickListener {
            sendReportPressed()
        }

        binding.uploadImageButton.setOnClickListener {
            uploadImagePressed()

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        binding.detailsEdtxtview.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.detailsEdtxtview.setRawInputType(InputType.TYPE_CLASS_TEXT)
    }

    private fun sendReportPressed() {
        if (!isSendingReport) {
            isSendingReport = true
        }

        //1. First save report
        var reportString = binding.categoryEdtxtview.text.toString()
        reportString += " : "
        reportString += binding.detailsEdtxtview.text.toString()
        val reportID = UUID.randomUUID().toString()

        context?.let { thecontext ->
            val file = File(thecontext.filesDir?.absolutePath, "$reportID.txt")
            file.bufferedWriter().use {
                it.write(reportString)
            }
        }
        ReportTracker.reportNumber.incrementAndGet()

        //2.Next step is Send the saved Report
        val postParameters = mapOf(
            "application_id" to REPORT_APP_ID * REPORT_PROVIDER_ID,
            "report_id" to reportID,
            "report" to reportString
        )
        if (postParameters.isNotEmpty()) {
            //send report
            val connection = URL(API_URL).openConnection() as HttpsURLConnection
        }

        isSendingReport = false
        context?.let {
            val report = "Report: ${ReportTracker.reportNumber.get()}"
            val toast = Toast.makeText(
                it, "Thank you for your report.$report", Toast
                    .LENGTH_LONG
            )
            toast.show()
        }
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun uploadImagePressed() {
        selectImageFromGallery()
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun showUploadedFile(selectedImage: Uri) {
        //get filename
        val fileNameColumn = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        val nameCursor = activity?.contentResolver?.query(selectedImage, fileNameColumn,
        null, null, null)
        nameCursor?.moveToFirst()
        val nameIndex = nameCursor?.getColumnIndex(fileNameColumn[0])
        var filename = ""
        nameIndex?.let {
            filename = nameCursor.getString(it)
        }
        nameCursor?.close()

        //update UI with filename
        binding.uploadStatusTextview?.text = filename
    }
}