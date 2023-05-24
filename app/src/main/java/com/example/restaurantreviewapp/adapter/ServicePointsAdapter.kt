package com.example.restaurantreviewapp.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantreviewapp.model.Restaurant
import com.example.hotelreviewapp.databinding.ItemRecyclerBinding
import com.example.restaurantreviewapp.model.OnSpClickListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class ServicePointsAdapter(
    var onSpClickListener: OnSpClickListener,
    var servicePointList: ArrayList<Restaurant>
) :
    RecyclerView.Adapter<ServicePointsAdapter.ServicePointViewHolder>() {

    private var contxt: Context? = null

    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicePointViewHolder {
        contxt = parent.context
        //This line creates a LayoutInflater object using the context of the parent ViewGroup.
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerBinding.inflate(layoutInflater, parent, false)

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        return ServicePointViewHolder(binding)
    }

    /**
     * his code defines an onBindViewHolder function, which is called by the RecyclerView to bind
     * data to a ViewHolder. The function takes two parameters: a ServicePointViewHolder object
     * that represents the view that the data will be bound to, and an integer position that
     * represents the position of the data in the list.
     */
    override fun onBindViewHolder(holder: ServicePointViewHolder, position: Int) {

        with(holder) {
            with(servicePointList[position]) {
                // Populate UI of the recycler view item by taking data from the provided list
                binding.name.text = this.name
                binding.location.text = this.location
                binding.ratingBar.rating = this.rating

                if (!TextUtils.isEmpty(this.hotelImage)) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val maxDownloadSize = 5L * 1024 * 1024
                            val bytes = storageReference.child("images/" + hotelImage)
                                .getBytes(maxDownloadSize).await()
                            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            withContext(Dispatchers.Main) {
                                binding.imageView.setImageBitmap(bmp)
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                //Toast.makeText(requireActivity(), e.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                binding.root.setOnClickListener {
                    onSpClickListener.onItemClick(position)
                }

                binding.popupImage.setOnClickListener{
                    onSpClickListener.onLongClickListener(position, binding.popupImage)
                }

//                binding.root.setOn(OnItemLongClickListener { arg0, arg1, pos, id -> // TODO Auto-generated method stub
//
//                    true
//                })
            }
        }
    }

    /**
     * Override method which returns the list size of item in the recycler view
     * Called by the recycler view
     */
    override fun getItemCount(): Int = servicePointList.size

    /**
     * This code defines an inner class called ServicePointViewHolder.
     * The class takes a single parameter binding of type ItemRecyclerBinding,
     * a generated class that represents a single view in the RecyclerView.
     */
    inner class ServicePointViewHolder(val binding: ItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root)
}