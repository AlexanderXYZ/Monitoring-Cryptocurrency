package com.buslaev.monitoringcryptocurrency.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.models.profile.Contributor
import com.buslaev.monitoringcryptocurrency.models.profile.Organization
import kotlinx.android.synthetic.main.organization_item.view.*

class OrganizationAdapter(context: Context) : BaseAdapter() {

    private var mOrgList = emptyList<Organization>()

    private val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return mOrgList.size
    }

    override fun getItem(position: Int): Any {
        return mOrgList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.organization_item, parent, false)
        val org = getItem(position) as Organization
        rowView.profile_organizations_name.text = org.name
        rowView.profile_organizations_description.text = org.description
        return rowView
    }

    fun setOrganizationList(list: List<Organization>) {
        mOrgList = list
        notifyDataSetChanged()
    }
}