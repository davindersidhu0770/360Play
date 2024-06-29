package com.a360play.a360nautica.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseActivity
import com.a360play.a360nautica.data.booking.AccessoryDataResponse
import com.a360play.a360nautica.databinding.ActivityAccessorieslistingBinding
import com.a360play.a360nautica.extension.MyCustomObjectListener
import com.a360play.a360nautica.utils.CustomSnackbar
import com.a360play.a360nautica.view.adapter.AccesoriesGroupAdapter
import com.a360play.a360nautica.viewmodels.BookingViewModel
import com.a360play.a360nautica.viewmodels.LoginViewModel
import com.app.starterkit.network.Repository
import com.app.starterkit.network.RetrofitClient
import com.app.starterkit.utils.MyViewModelFactory
import com.app.starterkit.utils.Status
import java.lang.String
import kotlin.Array
import kotlin.Boolean
import kotlin.Int
import kotlin.getValue

class AccesoriesListingActivity : BaseActivity(), MyCustomObjectListener {

    lateinit var binding: ActivityAccessorieslistingBinding
    lateinit var shared: SharedPreferences
    val apiService = RetrofitClient.apiService
    val repository = Repository(apiService)
    val accessoriesList: ArrayList<AccessoryDataResponse> = ArrayList<AccessoryDataResponse>()
    var isFromAccessories = false
    var sAccesoriesList: Array<kotlin.String>? = null
    var accessories_id = ""
    var currency = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccessorieslistingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()
        hideStatusBar()
        bindObservers()
        listeners()
    }

    private fun initview() {
        shared = getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        currency = shared.getString("currency", "").toString()
        binding.tvcurrency.setText(currency)
        accessories_id = intent.getStringExtra("accessories_id").toString()
        if (accessories_id != "") {
            sAccesoriesList = accessories_id.split(",").toTypedArray()
        }

        if (checkForInternet(this)) {
            viewModel.getAccessoryList(shared.getString("countryId", "")!!)
        } else {
            CustomSnackbar.make(
                window.decorView.rootView as ViewGroup,
                "No Internet Connection..."
            ).show()
        }

    }

    private fun setAdapter() {

        val accesoriesGroupAdapter = AccesoriesGroupAdapter(
            applicationContext, accessoriesList, this@AccesoriesListingActivity, currency
        )
        binding.rvRecyclerview.setAdapter(accesoriesGroupAdapter)
        binding.rvRecyclerview.setLayoutManager(LinearLayoutManager(this@AccesoriesListingActivity))


    }

    private fun listeners() {

        binding.btnSelectaccessories.setOnClickListener({
            var accessoriesID = ""
            var currency = ""
            var price = 0

            for (i in accessoriesList.indices) {
                if (accessoriesList.get(i).isSelected) {
                    accessoriesID = accessoriesID + String.valueOf(
                        accessoriesList.get(i).Id
                    ) + "-" + accessoriesList.get(i).itemcount
                    price =
                        price + accessoriesList.get(i).itemcount * accessoriesList.get(i)
                            .Price
                    if (i < accessoriesList.size - 1) {
                        accessoriesID = "$accessoriesID,"
                    }
                }
            }
            isFromAccessories = true
            val intent = Intent()
            intent.putExtra("mList", accessoriesID)
            intent.putExtra("totalamount", price)
            intent.putExtra("currency", currency)
            intent.putExtra("selectedList", accessoriesList)
            setResult(200, intent)
            finish()
        })

        binding.ivBackfromaccess.setOnClickListener(View.OnClickListener {

            finish()
        })

    }

    private val viewModel: BookingViewModel by viewModels() {
        MyViewModelFactory(repository)
    }

    private fun setAccesoriesList() {

        /*  val accesoriesGroup = AccesoriesGroup(1, "Socks", "5", currency, 0, false)
          accessoriesList.add(accesoriesGroup)

          val accesoriesGroup11 = AccesoriesGroup(2, "Socks111", "9", currency, 0, false)
          accessoriesList.add(accesoriesGroup11)

          val accesoriesGroup1 = AccesoriesGroup(3, "Water 500ml", "2", currency, 0, false)
          accessoriesList.add(accesoriesGroup1)
  */
        if (sAccesoriesList != null) {
            if (sAccesoriesList!!.size > 0) {
                for (i in accessoriesList.indices) {
                    for (j in sAccesoriesList!!.indices) {
                        val s: Array<kotlin.String> =
                            sAccesoriesList!!.get(j).split("-").toTypedArray()
                        if (!s[0].isNullOrBlank()) {
                            if (accessoriesList.get(i).Id === s[0].toInt()) {
                                accessoriesList.get(i).itemcount = (s[1].toInt())
                                accessoriesList.get(i).isSelected = true
                            }
                        }
                    }
                }
            }
        }

        setAdapter()
    }


    private fun bindObservers() {
        viewModel.accessoryListResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    displayLoader()
                    Toast.makeText(
                        this@AccesoriesListingActivity,
                        "Processing.....",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                Status.SUCCEESS -> {
                    hideLoader()
                    if (it.data!!.status_message.equals("Success")) {
                        accessoriesList.addAll(it.data.data)
                        setAccesoriesList()

                    } else {
                        Toast.makeText(this, it.data.status_message, Toast.LENGTH_SHORT).show()
                    }

                }
                Status.ERROR -> {
                    hideLoader()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onItemCLick(id: Int, quantity: Int, ischecked: Boolean, position: Int) {


    }

}