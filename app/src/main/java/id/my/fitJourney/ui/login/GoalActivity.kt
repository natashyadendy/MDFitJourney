package id.my.fitJourney.ui.login

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityGoalBinding
import id.my.fitJourney.ui.main.MainActivity
import id.my.fitJourney.util.BiodataSharePref

class GoalActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGoalBinding
    private lateinit var weights: Array<Button>
    private var weightString = arrayOf("weight_loss", "health_maintenance", "muscle_gain")
    var weightStatus: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        weights = arrayOf(
            binding.btnWeight0,
            binding.btnWeight1,
            binding.btnWeight2)

        binding.btnWeight0.setOnClickListener {
            weightStatus = 0
            changeButtonStatus(weightStatus)
        }

        binding.btnWeight1.setOnClickListener {
            weightStatus = 1
            changeButtonStatus(weightStatus)
        }

        binding.btnWeight2.setOnClickListener {
            weightStatus = 2
            changeButtonStatus(weightStatus)
        }

        binding.btnNext.setOnClickListener {
            val biodataSharedPref = BiodataSharePref(this)
            biodataSharedPref.saveGoal(weightString[weightStatus])

            BiodataActivity.instance.finish()
            LevelActivity.instance.finish()
            startActivity(Intent(this@GoalActivity, MainActivity::class.java));
            finish()
        }
    }

    fun changeButtonStatus(index: Int){
        for(item in weights){
            item.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#838383"))
        }
        weights[index].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary))
    }
}