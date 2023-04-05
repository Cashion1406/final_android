package com.example.finalandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.finalandroid.API.APIRespository
import com.example.finalandroid.model.PostData
import com.example.finalandroid.model.Topic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ApiViewModel(application: Application) : AndroidViewModel(application) {


    private val respository: APIRespository = APIRespository()
    val APIPost: MutableLiveData<Response<PostData>> = MutableLiveData()
    val APIPostList: MutableLiveData<Response<List<PostData>>> = MutableLiveData()
    val topicList: MutableLiveData<Response<List<Topic>>> = MutableLiveData()

    val topic: MutableLiveData<Response<Topic>> = MutableLiveData()

    fun getpost() {

        viewModelScope.launch(Dispatchers.IO) {
            val post = respository.getPost()
            APIPost.postValue(post)
        }

    }

    fun getPostNum(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            val post = respository.getPostNum(id)
            APIPost.postValue(post)
        }
    }

    fun getPostBasedID(id: Int, sort: String, order: String) {

        viewModelScope.launch(Dispatchers.IO) {

            val post = respository.getPostWithID(id, sort, order)
            APIPostList.postValue(post)
        }
    }

    fun getPostListV2(id: Int, option: Map<String, String>) {

        viewModelScope.launch(Dispatchers.IO) {


            val post = respository.getPostWithIDV2(id, option)

            APIPostList.postValue(post)
        }
    }

    fun pushPost(post: PostData) {
        viewModelScope.launch(Dispatchers.IO) {

            val push = respository.pushPost(post)

            APIPost.postValue(push)
        }
    }

    fun getTopic() {
        viewModelScope.launch(Dispatchers.IO) {
            val getTopic = respository.getTopic()

            topicList.postValue(getTopic)

        }
    }

    fun getTopicbyID(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val getTopicById = respository.getTopicbyID(id)

            topic.postValue(getTopicById)

        }
    }
}