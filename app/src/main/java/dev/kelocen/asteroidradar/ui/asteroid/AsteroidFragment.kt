package dev.kelocen.asteroidradar.ui.asteroid

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.kelocen.asteroidradar.R
import dev.kelocen.asteroidradar.databinding.FragmentAsteroidBinding

/**
 * A [Fragment] subclass for [dev.kelocen.asteroidradar.data.models.Asteroid] objects.
 */
class AsteroidFragment : Fragment() {

    private lateinit var binding: FragmentAsteroidBinding

    private var asteroidAdapter = AsteroidAdapter(AsteroidAdapter.AsteroidListener { asteroid ->
        asteroidViewModel.onAsteroidClicked(asteroid)
    })

    private val asteroidViewModel: AsteroidViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = AsteroidViewModelFactory(application)
        ViewModelProvider(this, viewModelFactory).get(AsteroidViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_asteroid, container, false)
        setHasOptionsMenu(true)
        binding.statusLoadingWheel.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.asteroidViewModel = asteroidViewModel
        binding.asteroidRecycler.adapter = asteroidAdapter
        asteroidViewModel.asteroidsLiveData.observe(viewLifecycleOwner, { asteroids ->
            if (asteroids != null) {
                binding.statusLoadingWheel.visibility = View.GONE
                asteroidAdapter.asteroids = asteroids
            }
        })
        asteroidViewModel.navigateToAsteroidDetail.observe(viewLifecycleOwner, { selectedAsteroid ->
            selectedAsteroid?.let {
                this.findNavController().navigate(
                        AsteroidFragmentDirections.actionShowDetail(selectedAsteroid))
                asteroidViewModel.onAsteroidNavigated()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
