package id.my.fitJourney.ui.detailfood

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import id.my.fitJourney.ViewModelFactory
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.data.remote.response.CustomFoodResponseItem
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityDetailFoodBinding
import id.my.fitJourney.ui.main.MainActivity


class DetailFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFoodBinding
    private lateinit var viewModel: DetailFoodViewModel
    private lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)
        type = intent.extras?.getString(EXTRA_TYPE) ?: "Breakfast"

        setupView()
        binding.btnArrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val foodData: CustomFoodResponseItem? = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_FOOD_DATA, CustomFoodResponseItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_FOOD_DATA)
        }

        val favFoodData: FoodData? = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_FAV_FOOD_DATA, FoodData::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_FAV_FOOD_DATA)
        }

        if (foodData != null) setupFoodData(foodData) else favFoodData?.let { setupFavData(it) }

    }

    private fun setupFoodData(foodData: CustomFoodResponseItem) {
        var isFavorite = false
        foodData.name?.let { name ->
            viewModel.isFoodFavorite(name).observe(this) {
                isFavorite = it > 0
                if (isFavorite) {
                    binding.btnFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFavorite.context,
                            R.drawable.baseline_favorite_24
                        )
                    )
                } else {
                    binding.btnFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFavorite.context,
                            R.drawable.baseline_favorite_border_24
                        )
                    )
                }
            }
        }

        val favData = FoodData(
            name = foodData.name,
            description = foodData.description,
            recipeIngredientParts = foodData.recipeIngredientParts,
            recipeIngredientQuantities = foodData.recipeIngredientQuantities,
            images = foodData.images,
            recipeInstructions = foodData.recipeInstructions,
            recipeServings = foodData.recipeServings,
            recipeCategory = foodData.recipeCategory,
            totalTime = foodData.totalTime,
            sugarContent = foodData.sugarContent,
            cholesterolContent = foodData.cholesterolContent,
            saturatedFatContent = foodData.saturatedFatContent,
            proteinContent = foodData.proteinContent,
            sodiumContent = foodData.sodiumContent,
            calories = foodData.calories,
            carbohydrateContent = foodData.carbohydrateContent,
            fatContent = foodData.fatContent,
            fiberContent = foodData.fiberContent
        )

        binding.btnSave.setOnClickListener {
            try {
                viewModel.insertFoodData(favData, type)
                Toast.makeText(this, "Food saved", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.tvFoodName.text = foodData.name
        Glide.with(this)
            .load(foodData.images)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_place_holder)
            .error(R.drawable.ic_place_holder)
            .into(binding.ivFoodResult)

        binding.btnFavorite.setOnClickListener {
            if (isFavorite) {
                foodData.name?.let { name -> viewModel.deleteFavorite(name) }
            } else {
                viewModel.saveFavoriteFood(favData)
            }
        }

        binding.pbCalories.progress = foodData.calories?.toInt() ?: 0

        binding.quantityCalories.text =
            getString(R.string.calories_quantity, foodData.calories.toString())
           }

    private fun setupFavData(favData: FoodData) {
        var isFavorite = false
        favData.name?.let { name ->
            viewModel.isFoodFavorite(name).observe(this) {
                isFavorite = it > 0
                if (isFavorite) {
                    binding.btnFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFavorite.context,
                            R.drawable.baseline_favorite_24
                        )
                    )
                } else {
                    binding.btnFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFavorite.context,
                            R.drawable.baseline_favorite_border_24
                        )
                    )
                }
            }
        }

        binding.tvFoodName.text = favData.name
        Glide.with(this)
            .load(favData.images)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_place_holder)
            .error(R.drawable.ic_place_holder)
            .into(binding.ivFoodResult)

        binding.btnFavorite.setOnClickListener {
            if (isFavorite) {
                favData.name?.let { name -> viewModel.deleteFavorite(name) }
            } else {
                viewModel.saveFavoriteFood(favData)
            }
        }

        binding.pbCalories.progress = favData.calories?.toInt() ?: 0

        binding.quantityCalories.text =
            getString(R.string.calories_quantity, favData.calories.toString())
      }

    private fun obtainViewModel(activity: DetailFoodActivity): DetailFoodViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailFoodViewModel::class.java]
    }

    private fun textListBuilder(list: List<String>?): SpannableStringBuilder {
        val builder = SpannableStringBuilder()

        if (list != null) {
            for (item in list) {
                val formattedText = "• $item\n"
                builder.append(formattedText)
            }
        }
        return builder
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

    companion object {
        const val EXTRA_FOOD_DATA = "extra_food"
        const val EXTRA_FAV_FOOD_DATA = "extra_fav_food"
        const val EXTRA_TYPE = "extra_type"
    }
}