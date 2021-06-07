package com.samulit.halal_pay

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_change_image.*

class ChangeImageActivity : AppCompatActivity() {

    private var databaseReference: DatabaseReference? = null
    private var storageReference: StorageReference? = null
    private var storageTask: StorageTask<*>? = null
    private var contentURI: Uri? = null
    private var resultUri: Uri? = null

    private val PICK_FROM_GALLERY = 1
    private  var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_image)

        ChangeImage.setOnClickListener {
            Change_Profile_Image();
        }
    }

    // Change Image """"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
    private fun Change_Profile_Image() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_FROM_GALLERY)
    }


    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            contentURI = data.data

            CropImage.activity(contentURI)
                    .setAspectRatio(1, 1)
                    .start(this)

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                resultUri = result.uri

                SaveImageOnFirebaseStorage()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //val error = result.error
            }
        }
    }

    private fun SaveImageOnFirebaseStorage() {
        if (resultUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Updating Profile Picture")
            progressDialog.show()
            progressDialog.setCanceledOnTouchOutside(false)

            val UserID = FirebaseAuth.getInstance().currentUser!!.uid

            storageReference = FirebaseStorage.getInstance().reference.child("UserImage").child(UserID)
            databaseReference = FirebaseDatabase.getInstance().getReference("UserData").child(UserID)

            val fileReference: StorageReference = storageReference!!.child("ProfilePic")

            storageTask = fileReference.putFile(resultUri!!).addOnSuccessListener { taskSnapshot ->
                val urlTask = taskSnapshot.storage.downloadUrl
                while (!urlTask.isSuccessful);
                val uri = urlTask.result
                imageUri = uri.toString()
                databaseReference!!.child("userImage").setValue(imageUri)
                Toast.makeText(this, "Update Successfully!", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                onBackPressed()
            }.addOnFailureListener { e: Exception -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() }

        } else {
            Toast.makeText(this, "Select An Image", Toast.LENGTH_SHORT).show()
        }
    }
    // End Change Image """"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
}