package com.algorigo.algorigoutils

import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.algorigo.algorigoutils.databinding.ItemWifiBinding

class WifiAdapter(onItemClickListener: OnItemClickListener? = null) : RecyclerView.Adapter<WifiAdapter.WifiViewHolder>() {

    inner class WifiViewHolder(private val binding: ItemWifiBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(wifiList[adapterPosition])
            }
        }

        fun setScanResult(scanResult: ScanResult) {
            binding.wifiSsidView.text = scanResult.SSID
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
        val binding = ItemWifiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WifiViewHolder(binding)
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