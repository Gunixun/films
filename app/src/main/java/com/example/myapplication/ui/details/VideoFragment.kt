package com.example.myapplication.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentVideoBinding
import com.example.myapplication.model.Video


class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_PARAM = "video"

        fun newInstance(video: Video): VideoFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARG_PARAM, video)
            val fragment = VideoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val video = arguments?.getParcelable<Video>(ARG_PARAM)
        if (video != null) {
            binding.shortInfoTextview.text = video.title
            binding.bodyTextview.text = video.body
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}