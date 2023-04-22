package com.anish.app.users.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anish.app.users.R
import com.anish.app.users.databinding.ActivityMainBinding
import com.anish.app.users.databinding.LayoutUserInformationContainerBinding
import com.anish.app.users.db.RealmModelClass
import com.anish.app.users.db.UserModel
import com.anish.app.users.models.UsersModel
import com.anish.app.users.ui.create.CreateUserLayout
import com.anish.app.users.utils.interfaces.AdapterClickListener
import com.anish.app.users.utils.interfaces.BottomSheetInterface
import com.anish.app.users.utils.interfaces.DataAddedListener
import com.anish.app.users.utils.interfaces.DataListener
import com.anish.app.users.utils.layouts.BottomSheetLayout
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheet: BottomSheetLayout
    private val realmModelClass = RealmModelClass()
    private lateinit var adapter: UserListAdapter
    private var mList = ArrayList<UsersModel>()
    private var maxID = 0
    private var type = false
    private var count = 0
    private var mLastClickTime: Long = 0
    private var usersModel: UsersModel? = null
    private lateinit var createUserLayout: CreateUserLayout
    private val successMessage = MutableStateFlow<String>("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialsBottomSheetDialog()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initialsViews()

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            type = false
            usersModel = null
            bottomSheet.show(supportFragmentManager, "Create User")
        }

        binding.contentLayout.userListView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.fab.visibility == View.VISIBLE) {
                    binding.fab.hide()
                } else if (dy < 0 && binding.fab.visibility != View.VISIBLE) {
                    binding.fab.show()
                }
            }
        })

        getAllData()
        observerMessage()

    }

    private fun observerMessage() {
        lifecycleScope.launch {
            successMessage.collect {
                if (!it.isNullOrEmpty())
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun preventDoubleClick() {
        Handler(Looper.getMainLooper()).postDelayed({
            count = 0
        }, 1000)
    }

    private fun initialsViews() {
        adapter = UserListAdapter(this@MainActivity, mList, object : AdapterClickListener {
            override fun onClicked(user: UsersModel) {
                usersModel = user
                type = true

                if (count == 0)
                    bottomSheet.show(supportFragmentManager, "Update User ${user.name}")
                count++
                preventDoubleClick()
            }

        })
        binding.contentLayout.userListView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        binding.contentLayout.userListView.adapter = adapter
    }

    private fun initialsBottomSheetDialog() {
        bottomSheet =
            BottomSheetLayout(
                R.layout.layout_user_information_container,
                object : BottomSheetInterface {
                    override fun onViewCreated(view: View) {
                        createUserLayout = CreateUserLayout(
                            LayoutUserInformationContainerBinding.bind(view),
                            object : DataListener {

                                override fun onAdd(user: UsersModel) {
                                    lifecycleScope.launch {
                                        realmModelClass.addData(user, object : DataAddedListener {
                                            override fun onSuccess(message: String) {
                                                getAllData()
                                                successMessage.value = message
                                            }

                                            override fun onFailed(error: String) {
                                                successMessage.value = error
                                            }

                                        })
                                    }
                                    dismissDialog()
                                }

                                override fun onUpdate(user: UsersModel) {
                                    lifecycleScope.launch {
                                        realmModelClass.update(user, object : DataAddedListener {
                                            override fun onSuccess(message: String) {
                                                getAllData()
                                                successMessage.value = message

                                            }

                                            override fun onFailed(error: String) {

                                                successMessage.value = error
                                            }

                                        })
                                    }
                                    dismissDialog()
                                }

                            })

                        createUserLayout.maxId(maxID)
                        createUserLayout.setType(type)
                        if (usersModel != null)
                            createUserLayout.updateData(usersModel!!)

                    }

                    override fun onClicked() {


                    }


                })
    }

    fun dismissDialog() {
        bottomSheet.dismiss()
    }

    fun getAllData() {
        // fetch all objects of a type as a flow, asynchronously
        lifecycleScope.launch {
            mList.clear()
            val userFlow: Flow<ResultsChange<UserModel>> =
                realmModelClass.realm?.query<UserModel>()!!.asFlow()
            val asyncCall: Deferred<Unit> = async {
                userFlow.collect { results ->
                    when (results) {
                        // print out initial results
                        is InitialResults<UserModel> -> {
                            for (users in results.list) {
                                Log.e("RealmModelClass", "Frog: $users")
                                if (users.id == null) maxID = 0
                                else {
                                    if (maxID < users.id!!) {
                                        maxID = users.id!!
                                    }
                                }
                                val mode = UsersModel(
                                    id = users.id,
                                    name = users.name,
                                    email = users.email,
                                    address = users.address,
                                    gender = users.gender,
                                    description = users.description
                                )
                                mList.add(mode)
                            }

                            adapter.notifyDataSetChanged()
                            checkUI()
                        }
                        else -> {
                            // do nothing on changes
                        }
                    }
                }
            }
        }

    }

    private fun checkUI() {
        if (mList.isEmpty()) {

            binding.contentLayout.userListView.visibility = View.GONE
            binding.contentLayout.noData.visibility = View.VISIBLE
        } else {
            binding.contentLayout.userListView.visibility = View.VISIBLE
            binding.contentLayout.noData.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realmModelClass.closeRealm()
    }
}