package com.anish.app.users.ui.create

import com.anish.app.users.R
import com.anish.app.users.databinding.LayoutUserInformationContainerBinding
import com.anish.app.users.models.UsersModel
import com.anish.app.users.utils.DefaultInputType
import com.anish.app.users.utils.interfaces.DataListener
import com.anish.app.users.utils.layouts.ButtonComponent
import com.anish.app.users.utils.layouts.TextInputComponent


/** Layout component to add/ edit user. Component is used as bottomsheet to view and add data.*/
class CreateUserLayout(
    private val bind: LayoutUserInformationContainerBinding,
    dataInterface: DataListener
) {

    //type indicate if update:true, add:false
    private var btnLayout: ButtonComponent = ButtonComponent(bind.addUser)
    private var userNameLayout: TextInputComponent = TextInputComponent(bind.userNameContainer)
    private var emailLayout: TextInputComponent = TextInputComponent(bind.userEmailContainer)
    private var addressLayout: TextInputComponent = TextInputComponent(bind.userAddressContainer)
    private var descriptionLayout: TextInputComponent =
        TextInputComponent(bind.userDescriptionContainer)
    private lateinit var usersModel: UsersModel
    private var selectedGender = "Male"
    private var maxID = 0
    private var type = false

    init {

        userNameLayout.setHint("Name")
        emailLayout.setHint("Email")
        addressLayout.setHint("Address")
        descriptionLayout.setHint("Description")
        userNameLayout.setInputType(DefaultInputType.FullName)
        emailLayout.setInputType(DefaultInputType.Email)
        addressLayout.setInputType(DefaultInputType.CapSentence)
        descriptionLayout.setInputType(DefaultInputType.CapSentence)
        radioButton()

        bind.userNameContainer
        btnLayout.onClicked {

            if (isValid()) {
                if (type) {
                    usersModel = UsersModel(
                        maxID,
                        userNameLayout.getText(),
                        emailLayout.getText(),
                        addressLayout.getText(),
                        selectedGender,
                        descriptionLayout.getText()
                    )
                    dataInterface.onUpdate(usersModel)
                } else {
                    usersModel = UsersModel(
                        maxID,
                        userNameLayout.getText(),
                        emailLayout.getText(),
                        addressLayout.getText(),
                        selectedGender,
                        descriptionLayout.getText()
                    )
                    dataInterface.onAdd(usersModel)
                }
            }
        }

    }

    /** type function is used whether to add or update data. Where add is false and update is true*/
    fun setType(t: Boolean) {
        type = t
        val titleText = if (type) "User Information" else "Create User"
        val btnText = if (type) "Update User" else "Create User"
        btnLayout.setText(btnText)
        bind.titleText.text = titleText
    }

    /** function to add max id to insert into database */
    fun maxId(id: Int): Int {
        maxID = id + 1
        return maxID
    }

    /** Update data and data binding for respective fields */
    fun updateData(usersModel: UsersModel) {
        maxID = usersModel.id!!
        this.usersModel = usersModel
        userNameLayout.setText(usersModel.name!!)
        emailLayout.setText(usersModel.email!!)
        addressLayout.setText(usersModel.address!!)
        descriptionLayout.setText(usersModel.description!!)
        when (usersModel.gender!!) {
            "Male" -> {
                bind.male.isChecked = true
                bind.female.isChecked = false
                bind.other.isChecked = false
            }
            "Female" -> {
                bind.male.isChecked = false
                bind.female.isChecked = true
                bind.other.isChecked = false

            }
            "Other" -> {
                bind.male.isChecked = false
                bind.female.isChecked = false
                bind.other.isChecked = true

            }
        }
    }

    private fun isValid(): Boolean {
        return if (!userNameLayout.isValid()) false
        else if (!userNameLayout.isValidName()) {
            userNameLayout.setError("Invalid name")
            false
        } else if (!emailLayout.isValid()) false
        else if (!emailLayout.isValidEmail()) {
            emailLayout.setError("Invalid email address")
            false
        } else if (!addressLayout.isValid()) false
        else if (!addressLayout.isValidAddress()) {
            addressLayout.setError("Invalid address")
            false
        } else if (selectedGender.isEmpty()) {
            false
        } else if (!descriptionLayout.isValid()) false
        else {
            true
        }
    }

    private fun radioButton() {

        bind.male.isChecked = true
        bind.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male -> selectedGender = "Male"
                R.id.female -> selectedGender = "Female"
                R.id.other -> selectedGender = "Other"
            }
        }
    }
}