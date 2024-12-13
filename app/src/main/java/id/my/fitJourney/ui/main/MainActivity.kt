package id.my.fitJourney.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import id.my.fitJourney.ViewModelFactory
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.ui.customfood.CustomFoodActivity
import id.my.fitJourney.ui.detailfood.DetailFoodActivity
import id.my.fitJourney.ui.login.LoginActivity
import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import id.my.fitJourney.ui.profile.ProfileActivity
import id.my.fitJourney.R
import id.my.fitJourney.data.remote.Result
import id.my.fitJourney.data.remote.model.SuggestionItem
import id.my.fitJourney.databinding.ActivityMainBinding
import id.my.fitJourney.ui.customfoodresult.CustomFoodResultActivity
import id.my.fitJourney.ui.foodjournal.FoodJournalActivity
import id.my.fitJourney.util.BiodataSharePref

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var auth: FirebaseAuth

    private lateinit var suggestionAdapter: SuggestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        viewModel = obtainViewModel(this)

        setupView()
        setupSuggestionRecyclerView()
        initSuggestionFood()
        setBindingView()
        setupProfile()


    }


    private fun setupProfile() {
        val user = auth.currentUser
        user?.let {
            binding.tvHomeTitle.text = getString(R.string.home_title, it.displayName)
            Glide.with(this)
                .load(it.photoUrl)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .error(R.drawable.profile)
                .into(binding.ivProfile)
        }
        binding.ivProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
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


    private fun initSuggestionFood(){
        val biodataSharedPref = BiodataSharePref(this).get()
        viewModel.suggestionFood(
            gender = biodataSharedPref[BiodataSharePref.GENDER].toString(),
            age = biodataSharedPref[BiodataSharePref.AGE]!!.toInt(),
            height = biodataSharedPref[BiodataSharePref.HEIGHT]!!.toInt(),
            weight = biodataSharedPref[BiodataSharePref.WEIGHT]!!.toInt(),
            activity = biodataSharedPref[BiodataSharePref.LEVEL].toString(),
            objective = biodataSharedPref[BiodataSharePref.GOAL].toString()
        ).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.consumed.text = "Food Suggestion"
                    binding.remaining.text = ""
                    Log.d("SUGGEST DATA", result.data.toString())
                    suggestionAdapter.Update(result.data)
//                    Toast.makeText(
//                        this@RegisterActivity,
//                        result.data.message, Toast.LENGTH_SHORT
//                    ).show()

                }

                is Result.Error -> {
                    binding.consumed.text = "Food Suggestion"
                    binding.remaining.text = " "
                    Log.d("SUGGEST DATA", result.error.toString())
                    Toast.makeText(this@MainActivity, result.error, Toast.LENGTH_SHORT)
                        .show()
                }

                is Result.Loading -> {
                    binding.consumed.text = "Food Sugestion"
                    binding.remaining.text = ""
                }

                is Result.Empty -> {
                    binding.consumed.text = "Food Suggestion"
                    binding.remaining.text = " "
                    Toast.makeText(
                        this,
                        getString(R.string.response_data_is_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupSuggestionRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSuggestion.layoutManager = layoutManager
        binding.rvSuggestion.setHasFixedSize(true)
        suggestionAdapter = SuggestionAdapter()
        binding.rvSuggestion.adapter = suggestionAdapter

//        val datas = ArrayList<SuggestionItem>()
//        datas.add(SuggestionItem(name = "Food SUggestion A", calories = 234.0))
//        datas.add(SuggestionItem(name = "Food SUggestion A", calories = 234.0))
//        datas.add(SuggestionItem(name = "Food SUggestion A", calories = 234.0))
//        datas.add(SuggestionItem(name = "Food SUggestion A", calories = 234.0))
//        datas.add(SuggestionItem(name = "Food SUggestion A", calories = 234.0))
//        datas.add(SuggestionItem(name = "Food SUggestion A", calories = 234.0))
//
//        suggestionAdapter.Update(datas)
    }



    private fun setBindingView() {
        binding.ibLogout.setOnClickListener {
            showLogoutDialog()
        }
        binding.btnCustomFood.setOnClickListener {
            val intent = Intent(this, CustomFoodResultActivity::class.java)
            intent.putExtra("type", "Breakfast")
            startActivity(intent)
        }
        binding.btnLunch.setOnClickListener {
            val intent = Intent(this, CustomFoodResultActivity::class.java)
            intent.putExtra("type", "Lunch")
            startActivity(intent)
        }
        binding.btnDinner.setOnClickListener {
            val intent = Intent(this, CustomFoodResultActivity::class.java)
            intent.putExtra("type", "Dinner")
            startActivity(intent)
        }
        binding.btnSnack.setOnClickListener {
            val intent = Intent(this, CustomFoodResultActivity::class.java)
            intent.putExtra("type", "Snack")
            startActivity(intent)
        }
        binding.btnFoodJournal.setOnClickListener {
            startActivity(Intent(this, FoodJournalActivity::class.java))
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.logout))
        builder.setMessage(getString(R.string.logout_message))
        builder.setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
            logout()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            alertDialog.findViewById<TextView>(android.R.id.message)?.setTextAppearance(
                R.style.DialogTextStyle
            )
            alertDialog.findViewById<TextView>(android.R.id.title)?.setTextAppearance(
                R.style.DialogTextStyle
            )
        }

        alertDialog.show()
    }


    private fun logout() {
        Firebase.auth.signOut()
        BiodataSharePref(this).remove()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }
}