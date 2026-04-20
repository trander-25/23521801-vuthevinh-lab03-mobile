package com.example.customer_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class UsersAdapter(
    context: Context,
    users: List<User>
) : ArrayAdapter<User>(context, 0, users) {

    private data class ViewHolder(
        val tvName: TextView,
        val tvHome: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView: View
        val holder: ViewHolder

        if (convertView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
            holder = ViewHolder(
                tvName = rowView.findViewById(R.id.tvName),
                tvHome = rowView.findViewById(R.id.tvHome)
            )
            rowView.tag = holder
        } else {
            rowView = convertView
            holder = rowView.tag as ViewHolder
        }

        val user = getItem(position)
        val name = user?.name.orEmpty()
        val hometown = user?.hometown.orEmpty()

        holder.tvName.text = if (name.isNotBlank() && hometown.isNotBlank()) {
            "$name from $hometown"
        } else {
            name.ifBlank { hometown }
        }
        holder.tvHome.text = ""

        return rowView
    }
}
