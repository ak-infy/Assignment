package com.example.interviewassignment.service.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.interviewassignment.R
import com.example.interviewassignment.databinding.ItemDataBinding
import com.example.interviewassignment.service.model.ListDataModel
import com.example.interviewassignment.viewmodel.ListDataViewModel

import kotlin.collections.ArrayList

//@TODO : Why is this not working with Comapnion object???
@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, imageUrl: String?) {
    Log.d("TESTING", "Image URL : " + imageUrl)
    imageUrl?. run {
        Glide.with(imageView.context)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_default_image)
                //@TODO: NEED To create GlideAppModule support
                //.diskCacheStrategyOf(DiskCacheStrategy.ALL)
            )
            .load(imageUrl)
            .into(imageView)
    }
}


class ListDataAdapter(private val listener: ListRowClickListener?): RecyclerView.Adapter<ListDataAdapter.DataViewHolder>() {
    interface ListRowClickListener {
        fun onListRowClicked(listDataModel: ListDataModel)
    }

    companion object{

    }

    // @TODO - WORK ON THIS LATER - Check Internal class difference and why the error
    class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var binding: ItemDataBinding? = null

        fun bind() = binding?: run { binding = DataBindingUtil.bind(itemView) }

        fun unbind() = binding?.unbind()

        fun setViewModel(viewModel: ListDataViewModel) = binding?. let {it.viewmodel = viewModel }

        init {
            bind()
        }
    }

    private val dataList = ArrayList<ListDataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_data,
            FrameLayout(parent.context), false
        )
        return DataViewHolder(itemView)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val listDataModel = dataList[position]
        holder.setViewModel(ListDataViewModel(listDataModel))
        holder.itemView.setOnClickListener { listener?.onListRowClicked(dataList[position]) }
    }

    fun updateDataInRecyclerView(updatedList: ArrayList<ListDataModel>) {
        dataList.clear()
        dataList.addAll(updatedList)
        notifyDataSetChanged()
    }

    //@TODO: CHECK IF THIS CAN BE DONE WITH LIFECYCLE AND IF IT WOULD BE BETTER THAT WAY
    override fun onViewAttachedToWindow(holder: DataViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.bind()
    }

    override fun onViewDetachedFromWindow(holder: DataViewHolder) {
        super.onViewDetachedFromWindow(holder)
        //@TODO
        // Why am I doing this here will the new view instance (new Component instance)
        // not be observing the cached data model
        holder.unbind()
    }

}
