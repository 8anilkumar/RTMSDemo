package com.anil.rtmsdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anil.rtmsdemo.R
import com.anil.rtmsdemo.models.ResponseData

class NestedAdapter(private val context: Context, private val mList: List<ResponseData>) : RecyclerView.Adapter<NestedAdapter.NestedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return NestedViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: NestedViewHolder, position: Int) {
        val responseList: ResponseData = mList[position]
        holder.timeSlotLayout?.background = context.getResources().getDrawable(R.color.white)
        if(responseList.status == 0){
            holder.timeSlotLayout?.background = context.getResources().getDrawable(R.color.light_grey)
        }

        val time = responseList.slot_start_time.take(2)
        val timeInSec = time.toInt()
        when(timeInSec){
            in 0..11->{
                holder.txtTime?.text = responseList.slot_start_time +"AM - "+responseList.slot_end_time+" AM"
            }
            in 11..23->{
                holder.txtTime?.text = responseList.slot_start_time +"PM - "+responseList.slot_end_time+" PM"
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class NestedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val txtTime: TextView? = itemView.findViewById(R.id.txtTime)
         val timeSlotLayout: LinearLayout? = itemView.findViewById(R.id.timeSlotLayout)
    }
}