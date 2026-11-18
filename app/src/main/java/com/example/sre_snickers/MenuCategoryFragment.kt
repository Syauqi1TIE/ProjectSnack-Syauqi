package com.example.sre_snickers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sre_snickers.databinding.FragmentMenuCategoryBinding

class MenuCategoryFragment : Fragment() {

    private var _binding: FragmentMenuCategoryBinding? = null
    private val binding get() = _binding!!

    private var categoryName: String = "Semua"

    companion object {
        private const val ARG_CATEGORY = "arg_category"

        fun newInstance(category: String): MenuCategoryFragment {
            val fragment = MenuCategoryFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryName = arguments?.getString(ARG_CATEGORY) ?: "Semua"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filteredProducts = if (categoryName == "Semua") {
            MenuData.products
        } else {
            MenuData.products.filter { it.category.equals(categoryName, ignoreCase = true) }
        }

        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.adapter = ProductAdapter(filteredProducts) { product ->
            Toast.makeText(
                requireContext(),
                "${product.name} (Rp ${"%,d".format(product.price).replace(',', '.')}) dipilih",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}