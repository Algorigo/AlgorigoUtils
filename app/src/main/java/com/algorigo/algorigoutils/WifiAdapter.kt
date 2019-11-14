package com.algorigo.algorigoutils

import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_wifi.view.*

class WifiAdapter(onItemClickListener: OnItemClickListener? = null) : RecyclerView.Adapter<WifiAdapter.WifiViewHolder>() {

    inner class WifiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(wifiList[adapterPosition])
            }
        }

        fun setScanResult(scanResult: ScanResult) {
            itemView.wifiSsidView.text = scanResult.SSID
        }
    }

    interface OnItemClickListener {
        fun onItemClick(scanResult: ScanResult)
    }

    private var onItemClickListener: OnItemClickListener? = null
    var wifiList = mutableListOf<ScanResult>()

    init {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wifi, parent, false)
        return WifiViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return wifiList.size
    }

    override fun onBindViewHolder(holder: WifiViewHolder, position: Int) {
        holder.setScanResult(wifiList[position])
    }

    fun setData(wifiList: List<ScanResult>) {
        this.wifiList = mutableListOf()
        this.wifiList.addAll(wifiList)
        notifyDataSetChanged()
    }
}