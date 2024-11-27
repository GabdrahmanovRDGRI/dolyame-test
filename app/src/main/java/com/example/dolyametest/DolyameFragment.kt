package com.example.dolyametest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.dolyametest.databinding.FragmentDolyameBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.tinkoff.dolyame.sdk.DolyameObserver
import ru.tinkoff.dolyame.sdk.DolyameResult
import ru.tinkoff.dolyame.sdk.DolyameSdk
import ru.tinkoff.dolyame.sdk.domain.model.DolyamePurchase

class DolyameFragment : Fragment(R.layout.fragment_dolyame) {

    private var _binding: FragmentDolyameBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val currentStatusFlow = MutableStateFlow("Nothing")

    private val dolyameObserver by lazy {
        DolyameObserver(requireActivity().activityResultRegistry, ::handleDolyameResult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(dolyameObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return FragmentDolyameBinding.inflate(inflater, container, false)
            .also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val purchase = DolyamePurchase(...)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    currentStatusFlow.collect {
                        binding.statusText.text = it
                    }
                }
            }
        }
        binding.startButton.setOnClickListener {
            DolyameSdk.openDolyame(
                requireActivity(),
                dolyameObserver,
                purchase
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleDolyameResult(result: DolyameResult) {
        currentStatusFlow.value = when (result) {
            DolyameResult.Success -> "Success"
            DolyameResult.Dismiss -> "Dismissed"
            is DolyameResult.Failure -> "Failure"
        }
    }

}