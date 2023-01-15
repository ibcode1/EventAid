package com.ib.eventaid.sharing.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.ib.eventaid.common.utils.setImage
import com.ib.eventaid.sharing.databinding.FragmentSharingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SharingFragment : Fragment() {

    companion object{
        const val EVENT_ID="id"
    }

    private val binding get() = _binding!!
    private var _binding:FragmentSharingBinding? = null
    private val viewModel by viewModels<SharingFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentSharingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        subscribeToViewStateUpdate()

    }

    private fun setupUI() {
        val eventId = requireArguments().getInt(EVENT_ID)
        viewModel.onEvent(SharingEvent.GetEventToShare(eventId))

        binding.shareButton.setOnClickListener{
            Snackbar.make(requireView(),"Shared! Or not :]", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun subscribeToViewStateUpdate(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.viewState.collect{
                    render(it)
                }
            }
        }
    }

    private fun render(viewState: SharingViewState) {
        val (image, message) = viewState.eventToShare

        binding.image.setImage(image)
        binding.messageToShareEditText.setText(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}