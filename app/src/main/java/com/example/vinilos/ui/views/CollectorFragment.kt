package com.example.vinilos.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.data.models.Collector
import com.example.vinilos.databinding.FragmentCollectorBinding
import com.example.vinilos.ui.viewmodels.CollectorViewModel
import com.example.vinilos.ui.views.adapters.CollectorAdapter

class CollectorFragment : Fragment() {

    private var _binding: FragmentCollectorBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorViewModel
    private var collectorAdapter: CollectorAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorBinding.inflate(inflater, container, false)
        val view = binding.root

        collectorAdapter = CollectorAdapter { selectedCollector ->
            Toast.makeText(context, "Clic en: ${selectedCollector.name}", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerViewCollectors
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = collectorAdapter

        binding.logoutIconCollector.setOnClickListener {
            navigateToHomeScreen()
        }


        binding.searchViewCollector.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                // El filtrado se manejará observando el LiveData filtrado o aplicando el filtro en el observer de 'collectors'
                return true
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        viewModel = ViewModelProvider(this, CollectorViewModel.Factory(activity.application))
            .get(CollectorViewModel::class.java)

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.collectors.observe(viewLifecycleOwner, Observer { collectorList ->
            collectorList?.let {

                val query = viewModel.searchQuery.value ?: ""
                val filteredList = if (query.isEmpty()) {
                    it
                } else {
                    it.filter { collector ->
                        collector.name.contains(query, ignoreCase = true) ||
                                collector.email.contains(query, ignoreCase = true)

                    }
                }

                collectorAdapter?.submitList(filteredList) // Usa submitList con ListAdapter
                binding.recyclerViewCollectors.visibility = if (filteredList.isEmpty() && query.isNotEmpty()) View.GONE else View.VISIBLE
                binding.textViewCollectorsError.visibility = if (filteredList.isEmpty() && query.isNotEmpty()) {
                    binding.textViewCollectorsError.text = "No se encontraron coleccionistas para \"$query\"" // Personaliza mensaje
                    View.VISIBLE
                } else if (filteredList.isEmpty() && query.isEmpty() && !viewModel.isLoading.value!! && viewModel.error.value == null){
                    binding.textViewCollectorsError.text = "No hay coleccionistas disponibles." // Mensaje lista vacía
                    View.VISIBLE
                }
                else {
                    View.GONE
                }
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                binding.progressBarCollectors.visibility = if (it) View.VISIBLE else View.GONE
                if (it) { // Si está cargando, ocultar error y lista temporalmente
                    binding.recyclerViewCollectors.visibility = View.GONE
                    binding.textViewCollectorsError.visibility = View.GONE
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            error?.let {

                if(!viewModel.isLoading.value!!) {
                    binding.textViewCollectorsError.text = "Error: ${it.message ?: "Error desconocido al cargar datos."}"
                    binding.textViewCollectorsError.visibility = View.VISIBLE
                    binding.recyclerViewCollectors.visibility = View.GONE // Ocultar lista en caso de error
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    viewModel.onClearedError() // Limpiar el error para que no se muestre de nuevo en re-creaciones
                }
            }
        })
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(activity, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.adapter = null
        collectorAdapter = null
        _binding = null
    }
}
