package com.example.restaurantreviewapp.fragments

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurantreviewapp.utils.PathUtil
import com.example.hotelreviewapp.R
import com.example.restaurantreviewapp.model.Restaurant
import com.example.hotelreviewapp.databinding.FragmentGalleryBinding
import com.example.restaurantreviewapp.helper.HomeViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*


class ViewHotelFragment : Fragment() {

    private var uploadPath: String = ""
    private lateinit var binding: FragmentGalleryBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var imagePath = ""
    private var imageName = ""
    private lateinit var viewModel: HomeViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    private fun checkPermissions(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                1001
            )
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val maxDownloadSize = 5L * 1024 * 1024
                val bytes =
                    storageReference.child("images/" + viewModel.getHotel().value?.hotelImage)
                        .getBytes(maxDownloadSize).await()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                withContext(Dispatchers.Main) {
                    binding.hotelImage.setImageBitmap(bmp)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireActivity(), e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.hotelImage.setOnClickListener {
            if (checkPermissions())
                imageChooser()
        }

        binding.ratingBar.numStars = 5
        binding.ratingBar.rating = viewModel.getHotel().value!!.rating
        binding.name.setText(viewModel.getHotel().value!!.name)
        binding.location.setText(viewModel.getHotel().value!!.location)
        binding.description.setText(viewModel.getHotel().value!!.description)
        viewModel.setLatLng(
            LatLng(
                viewModel.getHotel().value!!.lat,
                viewModel.getHotel().value!!.lon
            )
        )
        uploadPath = viewModel.getHotel().value!!.hotelImage

        binding.selectLocationButton.setOnClickListener {
            findNavController().navigate(R.id.action_viewHotelFragment_to_locationFragment)
        }

        binding.saveButton.setOnClickListener {
            val name = binding.name.text
            val locationDesc = binding.location.text
            val desc = binding.description.text
            val rating = binding.ratingBar.rating

            if (TextUtils.isEmpty(name)) {
                binding.name.error = "Please add a Name"
                binding.name.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(locationDesc)) {
                binding.location.error = "Please add Location name"
                binding.location.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(desc)) {
                binding.description.error = "Please add hotel description"
                binding.description.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(imageName) && TextUtils.isEmpty(uploadPath)) {
                Toast.makeText(requireActivity(), "Please Select an Image", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(viewModel.getLatLng().toString())) {
                Toast.makeText(requireActivity(), "Please Select Location", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val restaurant = Restaurant(
                viewModel.getHotel().value!!.id.toString(),
                name.toString(),
                locationDesc.toString(),
                viewModel.getLatLng().value!!.latitude,
                viewModel.getLatLng().value!!.longitude,
                uploadPath,
                rating,
                desc.toString()
            )

            uploadImage(restaurant)
        }
    }

    fun uploadData(restaurant: Restaurant) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Restaurants")
        val users: HashMap<String, Restaurant> = HashMap()
        val date = Date()
        date.time.toString()

        myRef.child(restaurant.id).setValue(restaurant)
        Toast.makeText(requireActivity(), "Updated", Toast.LENGTH_SHORT).show()
    }

    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchGallery.launch(i)
    }

    private var launchGallery = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // do your operation from here....
            if (data != null && data.data != null) {
                val selectedImageUri = data.data
                imagePath = PathUtil.getPath(requireActivity(), selectedImageUri)
                imageName = File(imagePath).name
                Toast.makeText(requireActivity(), "Image name: " + imageName, Toast.LENGTH_SHORT)
                    .show()
                selectedImageUri.let {
                    setImageBitmap(it!!)
                }
            }
        }
    }


    private fun setImageBitmap(uri: Uri) {
        val selectedImageBitmap: Bitmap = MediaStore.Images.Media.getBitmap(
            requireActivity().contentResolver, uri
        )

        binding.hotelImage.setImageBitmap(selectedImageBitmap)
    }

    // UploadImage method
    private fun uploadImage(restaurant: Restaurant) {
        if (imagePath != null) {

            // Code for showing progressDialog while uploading

            // Defining the child of storageReference
            if (!TextUtils.isEmpty(uploadPath) && TextUtils.isEmpty(imagePath)) {
                restaurant.hotelImage = uploadPath
                uploadData(restaurant)
            } else {
                val progressDialog = ProgressDialog(requireActivity())
                progressDialog.setTitle("Uploading...")
                progressDialog.show()
                uploadPath = UUID.randomUUID().toString() + imageName
                val ref = storageReference
                    .child(
                        "images/"
                                + uploadPath
                    )

                // adding listeners on upload
                // or failure of image

                ref.putFile(Uri.fromFile(File(imagePath)))
                    .addOnSuccessListener {
                        restaurant.hotelImage = uploadPath
                        uploadData(restaurant)
                        // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss()
                        Toast
                            .makeText(
                                requireActivity(),
                                "Image Uploaded!!",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                    .addOnFailureListener { e -> // Error, Image not uploaded
                        progressDialog.dismiss()
                        Toast
                            .makeText(
                                requireActivity(),
                                "Failed " + e.message,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        // Progress Listener for loading
                        // percentage on the dialog box
                        val progress = (100.0
                                * taskSnapshot.bytesTransferred
                                / taskSnapshot.totalByteCount)
                        progressDialog.setMessage(
                            "Uploaded "
                                    + progress.toInt() + "%"
                        )
                    }
            }
        }
    }

}