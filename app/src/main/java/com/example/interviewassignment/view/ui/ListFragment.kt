package com.example.interviewassignment.view.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.interviewassignment.R
import com.example.interviewassignment.service.adapter.ListDataAdapter
import com.example.interviewassignment.service.model.ListDataModel
import com.example.interviewassignment.service.model.ListModel
import com.example.interviewassignment.utils.ToastUtils
import com.example.interviewassignment.utils.isNetworkAvailable
import com.example.interviewassignment.viewmodel.MainViewModel

class ListFragment: Fragment(), View.OnClickListener, ListDataAdapter.ListRowClickListener {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var noInternetLayout: RelativeLayout
    private lateinit var btnRetry: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MainViewModel
    override fun onClick(p0: View?) {
        when (view?.id) {
            R.id.btnRetry -> {
                swipeRefreshLayout.isRefreshing = true
                noInternetLayout.visibility = View.GONE
                fetchDataList()
            }
            else -> {
                // DoNothing
            }
        }
    }

    override fun onListRowClicked(listDataModel: ListDataModel) {
        showToast("Clicked On: " + listDataModel.title)
    }

    private fun showToast(message: String) {
        activity?.let {
            ToastUtils.showToast(it, message)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        initRecyclerView(view)
        return view
    }

    private fun initRecyclerView(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        btnRetry = view.findViewById(R.id.btnRetry)
        btnRetry.setOnClickListener(this)
        recyclerView = view.findViewById(R.id.data_recycler_view)
        noInternetLayout = view.findViewById(R.id.noInternetLayout)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        swipeRefreshLayout.isRefreshing = true
        fetchDataList()
        swipeRefreshLayout.setOnRefreshListener { fetchDataList() }
    }


    private fun fetchDataList() {
        val adapter = ListDataAdapter(this)
        recyclerView.adapter = adapter
        context
            ?. let { context ->
                val liveData: LiveData<ListModel?>?
                val message = if (isNetworkAvailable(context)) {
                    liveData = viewModel.data
                    resources.getString(R.string.online_data_message)
                } else {
                    liveData = viewModel.offlineData
                    resources.getString(R.string.cached_data_message)
                }

                swipeRefreshLayout.isRefreshing = false
                noInternetLayout.visibility = View.VISIBLE

                liveData?.observe(viewLifecycleOwner,
                    Observer { listModel ->
                        val toastMessage = listModel
                            ?.let {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    activity?.title = listModel.title
                                }
                                adapter.updateDataInRecyclerView(it.rows)
                                message
                            }
                            ?: resources.getString(R.string.backend_error_message)
                        showToast(toastMessage)
                        noInternetLayout.visibility = View.GONE
                    })
                    ?: showToast(resources.getString(R.string.not_cached_message))
            }
    }
}