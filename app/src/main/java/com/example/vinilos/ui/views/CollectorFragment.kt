package com.example.vinilos.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.common.Constant
import com.example.vinilos.databinding.FragmentCollectorBinding
import com.example.vinilos.ui.viewmodels.CollectorViewModel
import com.example.vinilos.ui.adapters.CollectorAdapter
import java.text.Normalizer
import java.util.Locale

class CollectorFragment : Fragment() {

    private var _binding: FragmentCollectorBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorViewModel
    private lateinit var viewModelAdapter: CollectorAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModelAdapter = CollectorAdapter { collector ->
            val intent = Intent(requireContext(), CollectorDetailActivity::class.java)
            intent.putExtra(Constant.COLLECTOR_ID, collector.id)
            startActivity(intent)
        }

        progressBar = binding.progressBarCollectors
        recyclerView = binding.recyclerViewCollectors

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        // Configurar el color del texto en SearchView
        val searchEditText = binding.searchViewCollector.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_primary))
        searchEditText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.hint_text))

        binding.searchViewCollector.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCollectors(newText ?: "")
                return true
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(
            this,
            CollectorViewModel.Factory(activity.application)
        )[CollectorViewModel::class.java]

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        viewModel.collectors.observe(viewLifecycleOwner) { collectorList ->
            viewModelAdapter.submitList(collectorList)
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
            progressBar.visibility = View.GONE
        }
    }

    private fun filterCollectors(query: String) {
        val normalizedQuery = query.normalize()
        val filteredCollectors = viewModel.collectors.value?.filter { collector ->
            collector.name.normalize().contains(normalizedQuery)
        } ?: emptyList()
        val sortedFilterCollectors = filteredCollectors.sortedBy { it.name }
        viewModelAdapter.submitList(sortedFilterCollectors)
        binding.textViewCollectorsError.visibility = if (filteredCollectors.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun String.normalize(): String {
        return Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase(Locale.getDefault())
    }
}
