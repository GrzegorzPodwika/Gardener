package pl.podwikagrzegorz.gardener.ui.application_settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import pl.podwikagrzegorz.gardener.GardenerApp

import pl.podwikagrzegorz.gardener.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val darkThemeSwitch : SwitchMaterial = view.findViewById(R.id.dark_theme_switch)
        val preferenceRepository = (requireActivity().application as GardenerApp).preferenceRepository


        preferenceRepository.isDarkThemeLive.observe(viewLifecycleOwner, Observer { isDarkTheme ->
            isDarkTheme?.let { darkThemeSwitch.isChecked = it }
        })

        darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            preferenceRepository.isDarkTheme = checked
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
