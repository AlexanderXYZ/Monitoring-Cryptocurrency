package com.buslaev.monitoringcryptocurrency.screens.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.databinding.FragmentProfileBinding
import com.buslaev.monitoringcryptocurrency.models.profile.ProfileResponce
import com.buslaev.monitoringcryptocurrency.utilits.Resource


class ProfileFragment : Fragment() {

    private lateinit var mViewModel: ProflieViewModel
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
        mViewModel = ViewModelProvider(this).get(ProflieViewModel::class.java)
        val symbol: String = arguments?.getString("symbol") ?: "btc"
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
            val view = mBinding.profileOrganizationsNameHidden
            if (view.visibility == View.GONE) {
                view.visibility = View.VISIBLE
                mBinding.profileOrganizationsJurisdictionHidden.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
                mBinding.profileOrganizationsJurisdictionHidden.visibility = View.GONE
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
                    response.data?.let { profileResponce ->
                        val profile = profileResponce.data
                        mBinding.profileName.text = profile.name
                        mBinding.profileOverview.text = profile.overview
                        mBinding.profileHistoryDescription.text = profile.background
//                        mBinding.profileOrganizationsName.text =
//                            profile.organizations.elementAt(5).toString()
//                        mBinding.profileOrganizationsJurisdiction.text =
//                            profile.organizations.elementAt(3).toString()
                        //people
                        //mBinding.profilePeopleContributors
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}