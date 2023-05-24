package com.example.restaurantreviewapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hotelreviewapp.R
import com.example.hotelreviewapp.databinding.FragmentHomeBinding
import com.example.restaurantreviewapp.model.OnSpClickListener
import com.example.restaurantreviewapp.adapter.ServicePointsAdapter
import com.example.restaurantreviewapp.model.Restaurant
import com.example.restaurantreviewapp.helper.HomeViewModel

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class HomeFragment : Fragment(), OnSpClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var arrayList: ArrayList<Restaurant> = arrayListOf()
    private lateinit var adapter: ServicePointsAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        arrayList.clear()
        adapter = ServicePointsAdapter(this, arrayList)
        binding.recyclerView.adapter = adapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Restaurants")

        arrayList.clear()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arrayList.clear()
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataSnapshot.children.forEach {
                    val restaurant: Restaurant? = it.getValue(
                        Restaurant::class.java
                    )
                    arrayList.add(restaurant!!)
                    Log.d("HotelReviewApp", "Value is: ${restaurant.toString()}")
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    override fun onItemClick(position: Int) {
        viewModel.setHotel(arrayList.get(position))
        findNavController().navigate(R.id.action_nav_home_to_viewHotelFragment)
    }

    fun showPopupMenu(view: View, position: Int) {
        val popup = PopupMenu(requireActivity(), view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.delete_pop_up, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener { item ->
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("Restaurants")
            myRef.child(arrayList.get(position).id).removeValue()
            arrayList.removeAt(position)
            adapter.notifyItemRemoved(position)
            true
        }
        popup.show();
    }

    override fun onLongClickListener(position: Int, view: View) {
        super.onLongClickListener(position, view)
        showPopupMenu(view, position)
    }
}