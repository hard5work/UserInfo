package com.anish.app.users.db

import android.util.Log
import com.anish.app.users.models.UsersModel
import com.anish.app.users.utils.interfaces.DataAddedListener
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class RealmModelClass {
    var realm: Realm? = null

    init {
        val config = RealmConfiguration.Builder(setOf(UserModel::class))
            .name("User.db")
            .build()
        realm = Realm.open(config)
        Log.e("UserModel", "Successfully opened realm: ${realm?.configuration?.name}")
    }

    fun closeRealm() {
        realm?.close()
    }

    suspend fun addData(user: UsersModel, dataAddedListener: DataAddedListener) {

        try {
            realm?.write {
                this.copyToRealm(UserModel().apply {
                    id = user.id
                    name = user.name
                    email = user.email
                    address = user.address
                    description = user.description
                    gender = user.gender
                })
            }
            dataAddedListener.onSuccess("Successfully data added")
        } catch (e: Exception) {
            dataAddedListener.onFailed(e.message.toString())
            Log.e("UserModel", "Successfully hass been already added ${e.message.toString()}")
        }

    }

    suspend fun update(user: UsersModel, dataAddedListener: DataAddedListener) {
        realm?.write {
            // fetch a frog from the realm based on some query
            val userModel: UserModel? =
                this.query<UserModel>("id == ${user.id}").first().find()

            // if the query returned an object, update object from the query
            if (userModel != null) {
//                userModel.id = user.id
                userModel.name = user.name
                userModel.email = user.email
                userModel.address = user.address
                userModel.description = user.description
                userModel.gender = user.gender
                dataAddedListener.onSuccess("Data updated successfully")


            } else {
                dataAddedListener.onFailed("Sorry,Data not found")

            }
        }
    }

}