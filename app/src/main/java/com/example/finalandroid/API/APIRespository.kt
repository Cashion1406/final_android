package com.example.finalandroid.API

import com.example.finalandroid.model.Category
import com.example.finalandroid.model.PostData
import com.example.finalandroid.model.Topic
import retrofit2.Response

class APIRespository {

    suspend fun getPost(): Response<PostData> {
        return RetrofitInstance.api.getPost()

    }

    suspend fun getPostNum(id: Int): Response<PostData> {
        return RetrofitInstance.api.getPostNum(id)

    }

    suspend fun getPostWithID(id: Int, sort: String, order: String): Response<List<PostData>> {

        return RetrofitInstance.api.getListPost(id, sort, order)
    }

    suspend fun getPostWithIDV2(id: Int, options: Map<String, String>): Response<List<PostData>> {

        return RetrofitInstance.api.getListPostV2(id, options)
    }

    suspend fun pushPost(post: PostData): Response<PostData> {

        return RetrofitInstance.api.pushPost(post)
    }

    suspend fun getTopic(): Response<List<Topic>> {

        return RetrofitInstance.api.getallTopic()
    }

    suspend fun getTopicbyID(id: Int): Response<Topic>{

        return RetrofitInstance.api.getTopic(id)
    }


}