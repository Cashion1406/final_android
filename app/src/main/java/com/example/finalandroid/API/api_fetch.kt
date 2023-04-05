package com.example.finalandroid.API

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalandroid.R
import com.example.finalandroid.adapter.ApiAdapter
import com.example.finalandroid.viewmodel.ApiViewModel
import kotlinx.android.synthetic.main.fragment_api_fetch.*
import retrofit2.HttpException
import java.io.IOException


class api_fetch : Fragment() {

    private lateinit var apiViewModel: ApiViewModel
    private lateinit var option: HashMap<String, String>
    private lateinit var apiAdapter: ApiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiViewModel = ViewModelProvider(this)[ApiViewModel::class.java]
        option = HashMap()
        apiAdapter = ApiAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_api_fetch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPost()

        btn_get_post.setOnClickListener {


            val id = ed_post_num?.text.toString()
            if (id.isEmpty()) {

                getCate()
                return@setOnClickListener
            }
            getcateByID(id.toInt())
            option["_sort"] = "id"
            option["_order"] = "desc"

        }


    }

    private fun viewPost() {


        apiViewModel.topicList.observe(viewLifecycleOwner) { post ->

            if (post.isSuccessful) {
                post.body()?.let { apiAdapter.setPost(it) }
                rv_post_list.adapter = apiAdapter
                rv_post_list.layoutManager = LinearLayoutManager(requireContext())
            } else {
                Toast.makeText(requireContext(), post.code(), Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun getCate() {

        try {
            apiViewModel.getTopic()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("ERROR", e.message.toString())
        } catch (e: HttpException) {
            e.printStackTrace()
            Log.i("ERROR", e.message.toString())
        }
        apiViewModel.topicList.observe(viewLifecycleOwner) { response ->

            if (response.isSuccessful) {

                response.body()?.forEach { post ->
                    Log.i("API", post.name)
                    Log.i("API", post.id.toString())
                    Log.i("API", "------------")
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error where ${response.errorBody().toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
    }

    private fun getcateByID(id: Int) {

        try {
            apiViewModel.getTopicbyID(id)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("ERROR", e.message.toString())
        } catch (e: HttpException) {
            e.printStackTrace()
            Log.i("ERROR", e.message.toString())
        }
        apiViewModel.topic.observe(viewLifecycleOwner) { response ->

            if (response.isSuccessful) {


                topic_id.text = response.body()?.id.toString()
                topic_name.text = response.body()?.name

            } else {
                Toast.makeText(
                    requireContext(),
                    "Error where ${response.errorBody().toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
    }


}