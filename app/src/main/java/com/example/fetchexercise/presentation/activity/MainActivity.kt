package com.example.fetchexercise.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fetchexercise.databinding.ActivityMainBinding
import com.example.fetchexercise.presentation.adapter.GroupedAdapter
import com.example.fetchexercise.presentation.model.Group
import com.example.fetchexercise.presentation.model.UIState
import com.example.fetchexercise.presentation.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewmodel: MainActivityViewModel? by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        observeFlowData()
        fetchData()
        binding.retry.setOnClickListener {
            fetchData()
        }
    }

    private fun fetchData() {
        viewmodel?.loadData()
    }

    private fun observeFlowData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel?.dataFlow?.collect { state ->
                    when (state) {
                        is UIState.Loading -> {
                            showLoader()
                        }

                        is UIState.Success -> {
                            state.data?.let { loadData(it) } ?: showError()
                        }

                        is UIState.Error -> {
                            showError()
                        }
                    }
                }
            }
        }
    }

    private fun showLoader() {
        binding.errorCard.setVisible(false)
        binding.recyclerview.setVisible(false)
        binding.progressBar.setVisible(true)
    }

    private fun loadData(hiringList: List<Group>) {
        binding.progressBar.setVisible(false)
        binding.errorCard.setVisible(false)
        binding.recyclerview.setVisible(true)

        val adapter = GroupedAdapter(hiringList)
        binding.recyclerview.setAdapter(adapter)

    }

    private fun showError() {
        binding.progressBar.setVisible(false)
        binding.recyclerview.setVisible(false)
        binding.errorCard.setVisible(true)
    }

    private fun View.setVisible(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}