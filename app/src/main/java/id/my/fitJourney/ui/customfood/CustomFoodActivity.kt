package id.my.fitJourney.ui.customfood

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import id.my.fitJourney.ViewModelFactory
import id.my.fitJourney.data.remote.Result
import id.my.fitJourney.ui.customfoodresult.CustomFoodResultActivity
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityCustomFoodBinding


class CustomFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomFoodBinding
    private lateinit var viewModel: CustomFoodViewModel
    private lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        type = intent.extras?.getString("type") ?: "Breakfast"

        viewModel = obtainViewModel(this)

        setupBindingView()
        setupView()
    }

    private fun obtainViewModel(activity: CustomFoodActivity): CustomFoodViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[CustomFoodViewModel::class.java]
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupBindingView() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnGetMealsSugesstion.setOnClickListener {
            val caloriesValue = binding.sbCalories.progress


            viewModel.postCustomFood(
                caloriesValue,
            ).observe(this) { result ->
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        val intent = Intent(this, CustomFoodResultActivity::class.java)
                        intent.putExtra(
                            CustomFoodResultActivity.EXTRA_FOOD_DATA,
                            ArrayList(result.data)
                        )
                        intent.putExtra("type", type)
                        startActivity(intent)
                        binding.btnGetMealsSugesstion.isEnabled = true
                        seekbarEnabled(true)
                    }

                    is Result.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT)
                            .show()
                        showLoading(false)
                    }

                    is Result.Empty -> {
                        showLoading(false)
                    }

                    is Result.Loading -> {
                        seekbarEnabled(false)
                        binding.btnGetMealsSugesstion.isEnabled = false
                        showLoading(true)
                    }
                }
            }
        }

        initializeSeekBar(binding.sbCalories, binding.quantityCalories, R.string.calories_quantity)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun seekbarEnabled(isEnabled: Boolean) {
        binding.sbCalories.isEnabled = isEnabled
    }

    private fun initializeSeekBar(seekBar: SeekBar, textView: TextView, stringResId: Int) {
        textView.text = getString(stringResId, "0")
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textView.text = getString(stringResId, progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Implement logic when tracking starts
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Implement logic when tracking stops
            }
        })
    }
}