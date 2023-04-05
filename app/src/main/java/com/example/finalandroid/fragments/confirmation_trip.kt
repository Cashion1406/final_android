package com.example.finalandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalandroid.DAO.TripModel
import com.example.finalandroid.R
import com.example.finalandroid.viewmodel.TripViewModel
import kotlinx.android.synthetic.main.fragment_edittrip.cb_trip_info_risk
import kotlinx.android.synthetic.main.trip_confirmation_dialog.*


class confirmation_trip : DialogFragment() {

    private val args by navArgs<confirmation_tripArgs>()

    private lateinit var tripviewmodel: TripViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripviewmodel = ViewModelProvider(this)[TripViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.trip_confirmation_dialog, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setBackgroundDrawable(
            getDrawable(
                requireContext(),
                R.drawable.round_trip_detail
            )
        )
        finalTrip(tripModel = args.nowtrip)
        btn_add_confirm_trip.setOnClickListener {
            addTrip()
        }
        btn_cancle_confirm_add_trip.setOnClickListener {

            dialog?.dismiss()
        }

    }

    private fun addTrip() {
        tripviewmodel.addtrip(args.nowtrip)
        val action = confirmation_tripDirections.actionConfirmationTrip2ToDashboardFragment()
        findNavController().navigate(action)
    }

    private fun finalTrip(tripModel: TripModel) {
        tv_confirm_trip_name.setText(tripModel.name)
        tv_confirm_trip_destination.setText(tripModel.destination)
        tv_confirm_trip_date.setText(tripModel.date)
        tv_desc_confirm_trip.setText(tripModel.description)
        cb_trip_info_risk.isChecked = tripModel.riskmanagement == "true"

        for (rbPosition in 0 until rg_confirmation.childCount) {
            val rb = rg_confirmation.getChildAt(rbPosition) as RadioButton
            if (rb.text == tripModel.transpotation) {
                rb.visibility = View.VISIBLE
                rb.isChecked = true

            }
        }
    }


}