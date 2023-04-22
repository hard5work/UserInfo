package com.anish.app.users.db


import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow


open class UserModel : RealmObject {
    @PrimaryKey
    var id: Int? = null
    var name: String? = null
    var email: String? = null
    var address: String? = null
    var gender: String? = null
    var description: String? = null

    constructor(
        id: Int?,
        name: String?,
        email: String?,
        address: String?,
        gender: String?,
        description: String?
    ) {
        this.id = id
        this.name = name
        this.email = email
        this.address = address
        this.gender = gender
        this.description = description
    }


    constructor() {} // RealmObject subclasses must provide an empty constructor

    override fun toString(): String {
        return "UserModel(id=$id, name=$name, email=$email, address=$address, gender=$gender, description=$description)"
    }


}


