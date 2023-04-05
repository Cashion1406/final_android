package com.example.finalandroid.API

import com.example.finalandroid.model.PostData
import com.example.finalandroid.model.Topic
import retrofit2.Response
import retrofit2.http.*

interface TestAPI {

    @GET("posts/1")
    suspend fun getPost(): Response<PostData>

    @GET("posts/{postid}")
    suspend fun getPostNum(

        @Path("postid") id: Int
    ): Response<PostData>

    @GET("posts")
    suspend fun getListPost(

        @Query("userId") userId: Int,
        @Query("_sort") sort: String,
        @Query("_order") order: String

    ): Response<List<PostData>>

    //QueryMap
    @GET("posts")
    suspend fun getListPostV2(

        @Query("userId") userId: Int,
        @QueryMap options: Map<String, String>
    ): Response<List<PostData>>


    @FormUrlEncoded
    @POST("posts")
    suspend fun pushPost(
        @Body post: PostData
    ): Response<PostData>

    @GET("/topic")
    suspend fun getallTopic(): Response<List<Topic>>

    @GET("/topic/{id}")
    suspend fun getTopic(
        @Path("id") id: Int

    ): Response<Topic>
}