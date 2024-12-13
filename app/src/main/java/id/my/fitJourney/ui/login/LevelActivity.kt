package id.my.fitJourney.ui.login

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityLevelBinding
import id.my.fitJourney.util.BiodataSharePref

class LevelActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLevelBinding
    private lateinit var levels: Array<Button>
    private var levelString = arrayOf("sedentary", "lightly_active", "moderately_active", "very_active")
    var levelStatus: Int = 0;

    companion object{
        lateinit var instance: LevelActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instance = this

        levels = arrayOf(
            binding.btnLevel0,
            binding.btnLevel1,
            binding.btnLevel2,
            binding.btnLevel3)

        binding.btnLevel0.setOnClickListener {
            levelStatus = 0
            changeButtonStatus(levelStatus)
        }

        binding.btnLevel1.setOnClickListener {
            levelStatus = 1
            changeButtonStatus(levelStatus)
        }

        binding.btnLevel2.setOnClickListener {
            levelStatus = 2
            changeButtonStatus(levelStatus)
        }

        binding.btnLevel3.setOnClickListener {
            levelStatus = 3
            changeButtonStatus(levelStatus)
        }

        binding.btnNext.setOnClickListener {
            val biodataSharedPref = BiodataSharePref(this)
            biodataSharedPref.saveLevel(levelString[levelStatus])
            startActivity(Intent(this@LevelActivity, GoalActivity::class.java));
        }
    }

    fun changeButtonStatus(index: Int){
        for(item in levels){
            item.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#838383"))
        }
        levels[index].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary))
    }
}