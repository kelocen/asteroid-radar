package dev.kelocen.asteroidradar.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import dev.kelocen.asteroidradar.R
import dev.kelocen.asteroidradar.databinding.FragmentDetailBinding

/**
 * A [Fragment] subclass for displaying the details of selected [Asteroid][dev.kelocen.asteroidradar.domain.Asteroid] objects.
 */
class DetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid
        binding.asteroid = asteroid
        binding.imageHelpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }
        return binding.root
    }

    /**
     * Displays the an [AlertDialog] that defines the astronomical unit (au).
     */
    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomical_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
