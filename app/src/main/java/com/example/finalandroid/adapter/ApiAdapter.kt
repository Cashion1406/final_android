package com.example.finalandroid.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalandroid.R
import com.example.finalandroid.model.Category
import com.example.finalandroid.model.Topic
import kotlinx.android.synthetic.main.api_list_row.view.*


class ApiAdapter :
    RecyclerView.Adapter<ApiAdapter.ViewHolder>() {

    private var post = emptyList<Topic>()


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.api_list_row,
                parent,
                false
            )

        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = post[position]

        holder.itemView.cate_id.text = item.id.toString()

        holder.itemView.cate_name.text = item.name


    }


    override fun getItemCount(): Int {
        return post.size
    }

    fun setPost(post: List<Topic>) {
        this.post = post
        notifyDataSetChanged()

    }

}