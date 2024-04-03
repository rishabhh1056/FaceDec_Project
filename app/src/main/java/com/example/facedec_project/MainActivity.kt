package com.example.facedec_project

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.facedec_project.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnObj = Screendata("Capture Your Face")
        binding.text = btnObj
        binding.button.setOnClickListener {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            var text = TextView("Best Match!!")
//            binding.inputextview = text

            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, 123)
            } else {
                Toast.makeText(this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show()
            }
            Log.e("TAG", "onCreate: tap on btn ", )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==123 && resultCode == RESULT_OK) {

            val extras = data?.extras
            val bitmap = extras?.get("data")as? Bitmap

            if (bitmap != null) {
                detectFace(bitmap)
                Log.e("TAG", "onCreate: in on activity  ", )
            }
        }
    }

    private fun detectFace(bitmap: Bitmap)
    {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
        Log.e("TAG", "in dect face ", )
        Glide.with(this).load(bitmap).into(binding.pic)
        val Detector = FaceDetection.getClient(options)

        val image = InputImage.fromBitmap(bitmap, 0)

        val result = Detector.process(image)
            .addOnSuccessListener { faces ->
                // Task completed successfully
                Log.e("TAG", "in dect face result ", )
                var resultText = ""
                var faceCount = ""
                var smileCount = ""
                var leftEye = ""
                var RightEye = ""
                var i = 1
                for (face in faces)
                 {

                    faceCount = "$i"
                     smileCount= "${face.smilingProbability?.times(100)}"
                     leftEye= "${face.leftEyeOpenProbability?.times(100)}"
                     RightEye= "${face.rightEyeOpenProbability?.times(100)}"
                     /* resultText = "Face Number : $i"+
                             "\n Smile : ${face.smilingProbability?.times(100)}%"+
                               "\n Left Eye Open : ${face.leftEyeOpenProbability?.times(100)}%"+
                             "\n Right Eye Open : ${face.rightEyeOpenProbability?.times(100)}%"*/

                    i++
                }


                val smileImageList = listOf(R.drawable.smilingface1,
                    R.drawable.onemankysmile2,
                    R.drawable.onemonkysmiile,
                    R.drawable.smilingface2,
                    R.drawable.smilingface2,
                    R.drawable.smilingface3,
                    R.drawable.smilingface4,
                    R.drawable.smilingface5)

                val normalImageList = listOf(R.drawable.normalface2,
                    R.drawable.normalface3,
                    R.drawable.normalface4,
                    R.drawable.normalface5,
                    R.drawable.normalface6,
                    R.drawable.normalface4,
                    R.drawable.normalface2,
                    R.drawable.normalface9,
                    R.drawable.normalface10,
                    R.drawable.onenormalface1)

                val normalSmile = listOf(R.drawable.norsmil1,
                    R.drawable.norsmil2,
                    R.drawable.norsmil3,
                    R.drawable.norsmil4,
                    R.drawable.norsmil5,)

                val closeeyeList = listOf(R.drawable.closeeyewithsmile,
                    R.drawable.closeeyewithsmile2,
                    R.drawable.closeeyewithsmile3,)

                val twoSmilFaceList = listOf(R.drawable.twomonkeysmile,
                    R.drawable.twosmilingface1,
                    R.drawable.twosmile5,
                    R.drawable.twosmile6,
                    R.drawable.twosmile4,)

                val twoNorSmile = listOf(R.drawable.twonorsmile1,
                    R.drawable.twonorsmile2,
                    R.drawable.twosmile,
                    R.drawable.twonorsmile3,
                    R.drawable.twonorsmile4,
                )


                try {

                if (faces.isEmpty())
                {
                    val toast = Toast.makeText(this, "No Face Detect!!", Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                    Glide.with(this).load(R.drawable.notfound).into(binding.picmatch)
                }
                else {

                    if (faceCount.toDouble() == 1.0) {
                        if (smileCount.toDouble() >= 50.0 && leftEye.toDouble() < 10.0 || RightEye.toDouble() < 10.0) {
                            Glide.with(this).load(closeeyeList[getRandomNumbercloseeye()])
                                .into(binding.picmatch)
                        } else if (smileCount.toDouble() <= 20) {
                            Glide.with(this).load(normalImageList[getRandomNumberNormal()])
                                .into(binding.picmatch)
                        } else if (smileCount.toDouble() >= 20.0 && smileCount.toDouble() <= 96.0) {
                            Glide.with(this).load(normalSmile[getRandomNumberNormalSmile()])
                                .into(binding.picmatch)
                        } else if (smileCount.toDouble() >= 96) {
                            Glide.with(this).load(smileImageList[getRandomNumbersmile()])
                                .into(binding.picmatch)
                        } else {
                            Glide.with(this).load(R.drawable.notfound).into(binding.picmatch)
                        }


                    } else if (faceCount.toDouble() == 2.0) {
                        if (smileCount.toDouble() >= 96.0) {
                            Glide.with(this).load(twoSmilFaceList[getRandomNumberNormalSmile()])
                                .into(binding.picmatch)
                        } else if (smileCount.toDouble() in 20.0..96.0) {
                            Glide.with(this).load(twoNorSmile[getRandomNumberNormalSmile()])
                                .into(binding.picmatch)
                        } else if (smileCount.toDouble() <= 20) {
                            Glide.with(this).load(R.drawable.twosmile3).into(binding.picmatch)
                        } else {
                            Glide.with(this).load(R.drawable.notfound).into(binding.picmatch)
                        }
                    } else if (faceCount.toDouble() == 3.0) {

                        Glide.with(this).load(R.drawable.threenormalface).into(binding.picmatch)

                    } else if (faceCount.toDouble() == 4.0) {

                        Glide.with(this).load(R.drawable.fornormalface1).into(binding.picmatch)

                    } else {
                        Glide.with(this).load(R.drawable.notfound).into(binding.picmatch)
                    }
                }

                    binding.showfacecount.text = faceCount+"%"
                    binding.showsmile.text = smileCount+"%"
                    binding.showlefteye.text = leftEye+"%"
                    binding.showrighteye.text = RightEye+"%"
                    /*val toast =Toast.makeText(this, resultText, Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()*/
                }
                catch (e : Exception)
                {
                    val toast =Toast.makeText(this, "SomeThing Went Wrong!! ", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }

            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                Toast.makeText(this, "oops something error ${e.message.toString()}", Toast.LENGTH_SHORT).show()
            }

    }
    fun getRandomNumbersmile(): Int {
        require(0 <= 7) { "Invalid range, start must be less than or equal to end" }
        return Random.nextInt(0, 7 + 1)
    }
    fun getRandomNumberNormal(): Int {
        require(0 <= 9) { "Invalid range, start must be less than or equal to end" }
        return Random.nextInt(0, 9 + 1)
    }
    fun getRandomNumbercloseeye(): Int {
        require(0 <= 2) { "Invalid range, start must be less than or equal to end" }
        return Random.nextInt(0, 2 + 1)
    }

    fun getRandomNumberNormalSmile(): Int {
        require(0 <= 5) { "Invalid range, start must be less than or equal to end" }
        return Random.nextInt(0, 5+ 1)
    }


}

