package com.buslaev.monitoringcryptocurrency.screens.profile

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.buslaev.monitoringcryptocurrency.adapters.OrganizationAdapter
import com.buslaev.monitoringcryptocurrency.databinding.FragmentProfileBinding
import com.buslaev.monitoringcryptocurrency.models.profile.ProfileResponce
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import com.buslaev.monitoringcryptocurrency.utilits.SYMBOL_KEY


class ProfileFragment : Fragment() {

    private lateinit var mViewModel: ProfileViewModel
    private lateinit var mObserver: Observer<Resource<ProfileResponce>>

    private var _binding: FragmentProfileBinding? = null
    private val mBinding get() = _binding!!

    private val TAG = "profileCrypto"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        //return inflater.inflate(R.layout.fragment_profile, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val symbol: String = arguments?.getString(SYMBOL_KEY) ?: "btc"
        mViewModel.getProfile(symbol)

        initDataObserver()

        initExpends()
    }

    private fun initExpends() {
        //History
        mBinding.profileHistory.setOnClickListener {
            val view = mBinding.profileHistoryDescription
            if (view.visibility == View.GONE) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
        //Organization
        mBinding.profileOrganizations.setOnClickListener {
            val view = mBinding.profileOrganizationsListView
            if (view.visibility == View.GONE) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
        //Contributors
        mBinding.profilePeople.setOnClickListener {
            val view = mBinding.profilePeopleContributorsListview
            if (view.visibility == View.GONE) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }

    private fun initDataObserver() {
        mObserver = Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { profileResponse ->
                        val profile = profileResponse.data

                        mBinding.profileName.text = profile.name

                        val content = profile.overview
                        mBinding.profileOverview.text = parseHtml(content)

                        val history = profile.background
                        mBinding.profileHistoryDescription.text = parseHtml(history)


                        //Init organization list
                        val organizationsList = profile.organizations
                        val adapterOrg = OrganizationAdapter(requireContext(), organizationsList)
                        mBinding.profileOrganizationsListView.adapter = adapterOrg
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(TAG, "Error: $message")
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
        mViewModel.data.observe(viewLifecycleOwner, mObserver)
    }

    private fun parseHtml(content: String): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(content)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}