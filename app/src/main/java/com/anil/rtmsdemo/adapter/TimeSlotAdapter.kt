package com.anil.rtmsdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anil.rtmsdemo.R
import com.anil.rtmsdemo.models.TimeSlotModel

class TimeSlotAdapter(private val context: Context, private val responseDataList: List<TimeSlotModel>) : RecyclerView.Adapter<TimeSlotAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.time_slot_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val responseList: TimeSlotModel = responseDataList[position]
        var availableSlotCount = 0
        val response = responseDataList[position].timeSlotList

        for (item in response) {
            if (item.status == 1) {
                availableSlotCount++
            }
        }

        holder.txtAvailableSlot.text = "" + availableSlotCount + " Slot available"
        holder.categoryName.text = responseList.slatTime
        val adapter = NestedAdapter(context, responseDataList[position].timeSlotList)
        holder.timeSlotRecyclerView.setLayoutManager(LinearLayoutManager(holder.itemView.context))
        holder.timeSlotRecyclerView.setHasFixedSize(true)
        holder.timeSlotRecyclerView.setAdapter(adapter)

        val isExpandable: Boolean = responseList.isExpandable
        holder.expandableLayout.setVisibility(if (isExpandable) View.VISIBLE else View.GONE)

        if (isExpandable) {
            holder.mArrowImage.setImageResource(R.drawable.ic_expand)
        } else {
            holder.mArrowImage.setImageResource(R.drawable.ic_close)
        }

        holder.linearLayout.setOnClickListener {
            responseList.isExpandable = !isExpandable
            notifyItemChanged(holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int {
        return responseDataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.txtTime)
        val txtAvailableSlot: TextView = itemView.findViewById(R.id.txtAvailableSlot)
        val expandableLayout: LinearLayout = itemView.findViewById(R.id.expandableLayout)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
        val mArrowImage: ImageView = itemView.findViewById(R.id.mArrowImage)
        val timeSlotRecyclerView: RecyclerView = itemView.findViewById(R.id.timeSlotRecyclerView)
    }

}