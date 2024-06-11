package com.example.fetchexercise.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchexercise.R
import com.example.fetchexercise.data.model.HiringModel
import com.example.fetchexercise.presentation.model.Group

class GroupedAdapter(private val groups: List<Group>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (isGroupHeader(position)) TYPE_GROUP else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_GROUP) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
            GroupViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_GROUP) {
            (holder as GroupViewHolder).bind(groups[getGroupIndex(position)])
        } else {
            (holder as ItemViewHolder).bind(getItem(position))
        }
    }

    override fun getItemCount(): Int {
        return groups.sumOf { if (it.isExpanded) it.items.size + 1 else 1 }
    }

    private fun isGroupHeader(position: Int): Boolean {
        var count = 0
        for (group in groups) {
            if (position == count) return true
            count += if (group.isExpanded) group.items.size + 1 else 1
        }
        return false
    }

    private fun getGroupIndex(position: Int): Int {
        var count = 0
        for (i in groups.indices) {
            if (position == count) return i
            count += if (groups[i].isExpanded) groups[i].items.size + 1 else 1
        }
        throw IllegalStateException("Invalid position $position")
    }

    private fun getItem(position: Int): HiringModel {
        var count = 0
        for (group in groups) {
            count++
            if (group.isExpanded && position < count + group.items.size) {
                return group.items[position - count]
            }
            if (group.isExpanded) count += group.items.size
        }
        throw IllegalStateException("Invalid position $position")
    }

    companion object {
        private const val TYPE_GROUP = 0
        private const val TYPE_ITEM = 1
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupName: TextView = itemView.findViewById(R.id.groupName)
        fun bind(group: Group) {
            groupName.text = "Group ${group.listId}"
            itemView.setOnClickListener {
                group.isExpanded = !group.isExpanded
                notifyDataSetChanged()
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.itemName)
        fun bind(item: HiringModel) {
            itemName.text = item.name
        }
    }
}
