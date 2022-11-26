package com.ppm.selat.proof_booking

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ppm.selat.R
import com.ppm.selat.core.domain.model.OrderData
import com.ppm.selat.core.domain.model.UserData
import com.ppm.selat.databinding.ActivityProofBookingBinding
import com.ppm.selat.widget.onSnackError
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.w3c.dom.Text
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ProofBookingActivity : AppCompatActivity() {

    private val proofBookingViewModel : ProofBookingViewModel by viewModel()
    private lateinit var binding: ActivityProofBookingBinding
    private lateinit var orderData: OrderData
    private lateinit var fName: String
    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProofBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            proofBookingViewModel.getUser.collect {
                userData = it
            }
        }

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        if (!checkPermission()) {
            requestPermission()
        }

        supportActionBar?.hide()

        orderData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("ORDER_DATA", OrderData::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("ORDER_DATA")!!
        }

        setInvoice()

        fName = "invoice_selat_" + orderData.id

        setUpListener()
    }

    private fun setInvoice() {
        val splitPm = orderData.paymentTypeName.split(" ++ ")

        val pMethod = splitPm[1]
        val pMethodName = splitPm[0]

        val kursIndonesia2 = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp2 = DecimalFormatSymbols()

        formatRp2.currencySymbol = "Rp"
        formatRp2.monetaryDecimalSeparator = ','
        formatRp2.groupingSeparator = '.'

        kursIndonesia2.decimalFormatSymbols = formatRp2

        //layout.findViewById<TextView>()
        binding.userName.text = "${userData.name}\n${userData.email}"
        binding.invoiceNo.text  = "Invoice/Order No: ${orderData.id}"
        binding.dateOrder.text = "Date Order: ${orderData.dateOrder}"
        binding.numberValuePrice.text = orderData.paymentNumber
        binding.jnsPembayaran.text = "$pMethod ($pMethodName)"
        binding.brandManufacturerTarget.text = "${orderData.manufacturer} - ${orderData.brand}"
        binding.priceTarget.text = kursIndonesia2.format(orderData.price)
        binding.rentDaysTargetInvoice.text = "${orderData.rentDays} hari"
        binding.totalPrice.text = kursIndonesia2.format(orderData.rentDays * orderData.price)
    }

    private fun setUpListener() {
        val kursIndonesia1 = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp1 = DecimalFormatSymbols()

        formatRp1.currencySymbol = ""
        formatRp1.monetaryDecimalSeparator = ','
        formatRp1.groupingSeparator = '.'

        kursIndonesia1.decimalFormatSymbols = formatRp1

        val priceTotal = kursIndonesia1.format(orderData.rentDays * orderData.price).split(",")[0]
        val listPaymentDetail = orderData.paymentTypeName.split(" ++ ")

        with(binding) {
            binding.backButtonBp.setOnClickListener {
                finish()
            }
            binding.buttonSaveInvoice.setOnClickListener {
                createPdf()
            }
            tanggal.text = orderData.dateOrder
            orderNum.text = orderData.id
            brandTarget.text = orderData.brand
            manufacturerTarget.text = orderData.manufacturer
            rentDaysTarget.text = "${orderData.rentDays} Hari"
            priceTotalTarget.text = priceTotal
            priceTotalTarget2.text = priceTotal
            orderNumAccount.text =
                "Bayar Menggunakan\n${orderData.paymentNumber}\n${listPaymentDetail[1]}\n(${listPaymentDetail[0]})"

        }
    }

    private fun createPdf() {
        Dexter.withContext(this).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                @Override
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
//                            ConstraintLayout layout = findViewById(R.id.lay);
//                            ScrollView layout = findViewById(R.id.sView);
                        val layout = binding.invoiceDetail

                        val file: File? = saveBitMap(
                            this@ProofBookingActivity,
                            layout
                        ) //which view you want to pass that view as parameter
                        if (file != null) {
                            Log.i("TAG", "Drawing saved to the gallery!")
//                            Toast.makeText(
//                                this@ProofBookingActivity,
//                                "Sedang Menyimpan...",
//                                Toast.LENGTH_SHORT
//                            ).show()
                            try {
                                imageToPDF()
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            }
                        } else {
                            Log.i("TAG", "Oops! Image could not be saved.")
                            Toast.makeText(
                                this@ProofBookingActivity,
                                "Klik kembali untuk menyimpan invoice",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ProofBookingActivity,
                            "Permissions are not granted!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                @Override
                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    @Throws(FileNotFoundException::class)
    fun imageToPDF() {
        try {
            val document = Document()
            val dirpath = Environment.getExternalStorageDirectory().toString()
            PdfWriter.getInstance(
                document,
                FileOutputStream("$dirpath/$fName.pdf")
            ) //  Change pdf's name.
            document.open()
            val img = Image.getInstance(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + "/Pictures/Download/" + fName + ".jpg"
            )
            val scaler = (document.pageSize.width - document.leftMargin()
                    - document.rightMargin() - 0) / img.width * 100
            img.scalePercent(scaler)
            img.alignment = Image.ALIGN_CENTER or Image.ALIGN_TOP
            document.add(img)
            document.close()
            onSnackError("Invoice tersimpan, cek pada penyimpanan Internal untuk detail lebih lanjut", binding.root,  this@ProofBookingActivity)
        } catch (e: java.lang.Exception) {
            onSnackError("Invoice gagal tersimpan", binding.root,  this@ProofBookingActivity)
        }
    }

    private fun saveBitMap(context: Context, drawView: View): File? {
        val pictureFileDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "Download"
        ) // enter folder name to save image
        if (!pictureFileDir.exists()) {
            val isDirectoryCreated = pictureFileDir.mkdirs()
            if (!isDirectoryCreated) Log.i("ATG", "Tidak dapat memmbuat direktori untuk menyimpan gambar")
            return null
        }
        val filename = pictureFileDir.path + File.separator + fName + ".jpg"
        val pictureFile = File(filename)
        val bitmap: Bitmap? = getBitmapFromView(drawView)
        try {
            pictureFile.createNewFile()
            val oStream = FileOutputStream(pictureFile)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, oStream)
            oStream.flush()
            oStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("TAG", "There was an issue saving the image.")
        }
        scanGallery(context, pictureFile.absolutePath)
        return pictureFile
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

    private fun scanGallery(cntx: Context, path: String) {
        try {
            MediaScannerConnection.scanFile(
                cntx, arrayOf(path), null
            ) { path, uri -> }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result = ContextCompat.checkSelfPermission(
                this@ProofBookingActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val result1 = ContextCompat.checkSelfPermission(
                this@ProofBookingActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }


    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data =
                    Uri.parse(String.format("package:%s", applicationContext.packageName))
                startActivityForResult(intent, 2296)
            } catch (e: java.lang.Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 2296)
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(
                this@ProofBookingActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1100
            )
        }
    }
}