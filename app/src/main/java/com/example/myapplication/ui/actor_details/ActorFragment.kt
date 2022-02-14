package com.example.myapplication.ui.actor_details

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentActorBinding
import com.example.myapplication.model.Actor
import com.example.myapplication.model.ActorPreview
import com.example.myapplication.showSnackBar
import com.example.myapplication.ui.BaseFragmentWithModel
import com.example.myapplication.utils.DETAILS_POSTER_SIZE
import com.example.myapplication.utils.MAIN_POSTER_LINK
import com.example.myapplication.viewmodel.ActorViewModel
import com.example.myapplication.viewmodel.AppStateActor
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class ActorFragment :
    BaseFragmentWithModel<ActorViewModel, FragmentActorBinding>(FragmentActorBinding::inflate) {

    private lateinit var map: GoogleMap
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container,savedInstanceState )
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)
        return view
    }


    companion object {
        const val ARG_PARAM = "Actor ID"
        var actorId: String? = ""

        fun newInstance(actor: ActorPreview): ActorFragment {
            return ActorFragment().also { fragment ->
                fragment.arguments = Bundle().also { bundle ->
                    bundle.putString(ARG_PARAM, actor.id)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actorId = arguments?.getString(ARG_PARAM)
        if (actorId != null) {
            viewModel.getActor(actorId!!)
        }
        viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
            renderData(state)
        }
    }

    private fun renderData(state: AppStateActor) {
        binding.progress.isVisible = false
        when (state) {
            is AppStateActor.Success -> {
                setActor(state.actor)

            }
            is AppStateActor.Loading -> {
                binding.progress.isVisible = true
            }
            is AppStateActor.Error -> {
                binding.root.showSnackBar(
                    text = state.error.toString(),
                    actionText = R.string.retry,
                    { viewModel.getActor(actorId!!) }
                )
            }
        }
    }

    private fun setActor(actor: Actor){
        with(binding) {
            Glide
                .with(requireContext())
                .load("$MAIN_POSTER_LINK$DETAILS_POSTER_SIZE${actor.icon_path}")
                .into(imageView);
            nameTextview.text = actor.name
            biographyTextview.text = actor.biography
            birthdayTextview.text = "Дата рождения: ${actor.birthday}"

            val geoCoder = Geocoder(context)
            executor.execute {
                try {
                    val addresses = geoCoder.getFromLocationName(actor.place_of_birth, 1)
                    if (addresses.size > 0) {
                        handler.post{goToAddress(addresses, actor.place_of_birth)}

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun goToAddress(
        addresses: MutableList<Address>,
        searchText: String
    ) {
        val location = LatLng(
            addresses[0].latitude,
            addresses[0].longitude
        )
        setMarker(location, searchText)
        map.moveCamera(
            CameraUpdateFactory.newLatLng(location)
        )
    }

    private fun setMarker(
        location: LatLng,
        searchText: String
    ): Marker? {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
        )
    }
}