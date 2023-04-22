package com.anish.app.users.utils.interfaces

import android.view.View
import com.anish.app.users.models.UsersModel


interface BottomSheetInterface {
    fun onViewCreated(view: View)
    fun onClicked()
}

interface DataListener {
    fun onAdd(user: UsersModel)
    fun onUpdate(user: UsersModel)
}

interface DataAddedListener {
    fun onSuccess(message: String)
    fun onFailed(error: String)
}

interface AdapterClickListener {
    fun onClicked(user: UsersModel)
}