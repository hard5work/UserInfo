package com.anish.app.users.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.anish.app.users.R
import com.anish.app.users.databinding.LayoutUserListItemBinding
import com.anish.app.users.models.UsersModel
import com.anish.app.users.utils.interfaces.AdapterClickListener

class UserListAdapter(
    private val context: Context,
    private val mList: ArrayList<UsersModel>,
    private val adapterClickListener: AdapterClickListener
) : RecyclerView.Adapter<UserListAdapter.UserListHolder>() {

    inner class UserListHolder(item: LayoutUserListItemBinding) : ViewHolder(item.root) {
        val bind = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListHolder {
        return UserListHolder(
            LayoutUserListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: UserListHolder, position: Int) {
        val model = mList[position]
        holder.bind.userModel = model
        holder.bind.mainContainer.setOnClickListener {
            adapterClickListener.onClicked(model)
        }
        setDrawable(holder.bind.gender, model.gender!!)


    }

    fun setDrawable(textView: TextView, mode: String) {
        val drawable: Int = when (mode) {
            "Male" -> {
                R.drawable.ic_man
            }
            "Female" -> {
                R.drawable.ic_woman
            }
            "Other" -> {
                R.drawable.ic_transgender

            }
            else -> {
                R.drawable.ic_man
            }
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
    }
}