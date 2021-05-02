package dev.kelocen.asteroidradar.ui.asteroid

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.kelocen.asteroidradar.R
import dev.kelocen.asteroidradar.databinding.FragmentAsteroidBinding

class AsteroidFragment : Fragment() {

    private val viewModel: AsteroidViewModel by lazy {
        ViewModelProvider(this).get(AsteroidViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentAsteroidBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.asteroidViewModel = viewModel
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
