package com.example.finalandroid.fragments

import android.animation.LayoutTransition
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalandroid.DAO.TripModel
import com.example.finalandroid.MainActivity
import com.example.finalandroid.R
import com.example.finalandroid.WelcomeActivity
import com.example.finalandroid.adapter.tripAdapter
import com.example.finalandroid.viewmodel.ExpenseViewModel
import com.example.finalandroid.viewmodel.TripViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.alert_dialog.*
import kotlinx.android.synthetic.main.fragment_dashboard_fragment.*


class dashboard_fragment : Fragment() {
    private lateinit var tripAdapter: tripAdapter

    private lateinit var tripviewmode: TripViewModel

    private lateinit var expenseViewModel: ExpenseViewModel

    private lateinit var db: FirebaseFirestore

    private var user_name: String? = null

    private var UID: String? = null

    private var currentSearch: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]
        tripviewmode = ViewModelProvider(this)[TripViewModel::class.java]
        tripAdapter = tripAdapter()
        val sharedPreferences =
            requireActivity().getSharedPreferences("name", Context.MODE_PRIVATE)

        val realuser = FirebaseAuth.getInstance().currentUser
        if (realuser != null) {
            UID = realuser.uid

        } else if (sharedPreferences.contains("name_key")) {

            FancyToast.makeText(
                requireContext(),
                "Hello ${sharedPreferences.getString("name_key", null)}",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                false
            ).show()
        } else {
            startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
            requireActivity().finish()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).supportActionBar?.title = ""

        return inflater.inflate(R.layout.fragment_dashboard_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadusername()
        viewTrip()
        img_search.setOnClickListener {

            user_reveal.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            val reveal =
                if (reveal_search_bar.visibility == View.GONE) View.VISIBLE else View.GONE
            reveal_search_bar.visibility = reveal
            sv_trip.isIconified = false
        }

        sv_trip.setOnCloseListener {

            reveal_search_bar.visibility = View.GONE
            true
        }
        tv_user_name.setOnLongClickListener {
            click()
            return@setOnLongClickListener true
        }
        tv_user_name.setOnClickListener {
            val sharedPreferences =
                requireActivity().getSharedPreferences("name", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            user_name = null
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
            requireActivity().finish()

        }

        sv_trip.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    search(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    search(newText)
                }
                search(newText)
                return true
            }
        })

    }

    fun search(search: String) {
        tripviewmode.search("%$search%").observe(this) { result ->
            tripAdapter.setTrip(result)
        }
    }


    private fun loadusername() {
        db = FirebaseFirestore.getInstance()
        val sharedPreferences =
            requireActivity().getSharedPreferences(
                "name",
                Context.MODE_PRIVATE
            )
        //fetch user name preference from regigser
        if (sharedPreferences.contains("name_key")) {
            tv_user_name.text = sharedPreferences.getString("name_key", null)

        } else {
            //if login directly
            if (user_name == null) {
                if (context != null) {
                    db.collection("Users").document(UID.toString())
                        .get()
                        .addOnSuccessListener { task ->
                            user_name = task.get("Name").toString()
                            tv_user_name.text = user_name
                        }
                }
            } else {
                tv_user_name.text = user_name
            }
        }
    }


    fun viewTrip() {
        tripviewmode.getalltrip().observe(viewLifecycleOwner) { trip ->
            tripAdapter.setTrip(trip)
            sv_trip.queryHint = "${trip.size} trips available"
            if (trip.isNotEmpty()) {

                rv_trip.visibility = View.VISIBLE
                tv_no_trip_text.visibility = View.GONE
                rv_trip.adapter = tripAdapter
                rv_trip.layoutManager = LinearLayoutManager(requireContext())
                val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
                divider.setDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        androidx.appcompat.R.drawable.abc_list_divider_material
                    )!!
                )
                rv_trip.addItemDecoration(divider)//*

                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val tripos: TripModel = tripAdapter.gettrip(viewHolder.adapterPosition)
                        tripviewmode.deletetrip(tripos)

                        Snackbar.make(rv_trip, "Delete " + tripos.name, Snackbar.LENGTH_LONG)
                            .setAction(
                                "Undo"
                            ) {
                                tripviewmode.addtrip(tripos)

                            }.show()
                    }
                }).attachToRecyclerView(rv_trip)
            } else {
                rv_trip.visibility = View.GONE
                tv_no_trip_text.visibility = View.VISIBLE

            }

        }


    }


    fun click() {

        val alertDialog = Dialog(requireContext())

        alertDialog.setContentView(R.layout.alert_dialog)
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.bg_white_rounded)


        alertDialog.show()
        alertDialog.tv_no_backup.setOnClickListener {
            tripviewmode.deleteAll()
            alertDialog.dismiss()
        }

        alertDialog.tv_backup.setOnClickListener {
            val action = dashboard_fragmentDirections.actionDashboardFragmentToFragmentBackup()
            findNavController().navigate(action)
            alertDialog.dismiss()
        }

    }

}