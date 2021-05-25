package dev.kelocen.asteroidradar.ui.asteroid

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.kelocen.asteroidradar.R
import dev.kelocen.asteroidradar.data.AsteroidFilter
import dev.kelocen.asteroidradar.databinding.FragmentAsteroidBinding
import dev.kelocen.asteroidradar.domain.Asteroid
import dev.kelocen.asteroidradar.domain.PictureOfDay
import kotlinx.android.synthetic.main.picture_information_alert.view.*

/**
 * A [Fragment] subclass for [Asteroid][dev.kelocen.asteroidradar.domain.Asteroid] objects.
 */
class AsteroidFragment : Fragment() {

    private lateinit var binding: FragmentAsteroidBinding

    /**
     * An instance of [PictureOfDay] for the picture information alert dialog.
     */
    private var pictureOfDay: PictureOfDay? = null

    /**
     * An instance of [AsteroidAdapter] to listen for selected [Asteroid] objects.
     */
    private var asteroidAdapter = AsteroidAdapter(AsteroidAdapter.AsteroidListener { asteroid ->
        asteroidViewModel.onAsteroidClicked(asteroid)
    })

    /**
     * An instance of [AsteroidViewModel] to retrieve data for the UI.
     */
    private val asteroidViewModel: AsteroidViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = AsteroidViewModelFactory(application)
        ViewModelProvider(this, viewModelFactory).get(AsteroidViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_asteroid, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.asteroidViewModel = asteroidViewModel
        binding.asteroidRecycler.adapter = asteroidAdapter
        binding.statusLoadingWheel
        setupObservers()
        setupListeners()
    }

    /**
     * Configures the observers for the fragment.
     */
    private fun setupObservers() {
        asteroidViewModel.recyclerAsteroids.observe(viewLifecycleOwner, { asteroids ->
            if (asteroids != null) {
                asteroidAdapter.submitList(asteroids)
            }
        })
        asteroidViewModel.pictureOfDay.observe(viewLifecycleOwner, { picture ->
            pictureOfDay = picture
        })
        asteroidViewModel.navigateToAsteroidDetail.observe(viewLifecycleOwner, { selectedAsteroid ->
            selectedAsteroid?.let {
                this.findNavController()
                    .navigate(AsteroidFragmentDirections.actionShowDetail(selectedAsteroid))
                asteroidViewModel.onAsteroidNavigated()
            }
        })
    }

    /**
     * Configures the listeners for the fragment.
     */
    private fun setupListeners() {
        binding.refreshAsteroidRecycler.setOnRefreshListener {
            refreshAllContent()
        }

        /**
         * Shows an [AlertDialog] to display the title of the [PictureOfDay][dev.kelocen.asteroidradar.domain.PictureOfDay]
         * when the information image is tapped.
         */
        binding.imagePictureInfoButton.setOnClickListener {
            displayPictureInfoDialog()
        }
    }

    /**
     * Refreshes the content for the [PictureOfDay][dev.kelocen.asteroidradar.domain.PictureOfDay]
     * and [Asteroids][dev.kelocen.asteroidradar.domain.Asteroid].
     */
    private fun refreshAllContent() {
        asteroidViewModel.refreshAsteroidRepository()
        asteroidViewModel.refreshPictureOfDay()
        if (binding.refreshAsteroidRecycler.isRefreshing) {
            binding.refreshAsteroidRecycler.isRefreshing = false
        }
    }

    /**
     * Displays an [AlertDialog] that contains information about the [PictureOfDay][dev.kelocen.asteroidradar.domain.PictureOfDay]
     */
    private fun displayPictureInfoDialog() {
        val dialogLayout = layoutInflater.inflate(R.layout.picture_information_alert, null)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(dialogLayout)
        builder.setPositiveButton(android.R.string.ok, null).create()
        when (pictureOfDay?.mediaType) {
            "video" -> {
                dialogLayout.text_picture_of_day_name.text = getText(R.string.video_not_supported)
            }
            "image" -> {
                dialogLayout.text_picture_of_day_name.text = pictureOfDay?.title
            }
            else -> {
                dialogLayout.text_picture_of_day_name.text = getText(R.string.no_data_available)
            }
        }
        builder.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_today_menu -> {
                asteroidViewModel.updateAsteroidSelection(AsteroidFilter.SHOW_TODAY)
            }
            R.id.show_week_menu -> {
                asteroidViewModel.updateAsteroidSelection(AsteroidFilter.SHOW_WEEK)
            }
            R.id.show_all_menu -> {
                asteroidViewModel.updateAsteroidSelection(AsteroidFilter.SHOW_ALL)
            }
            R.id.refresh_menu -> {
                refreshAllContent()
            }
        }
        return true
    }
}
