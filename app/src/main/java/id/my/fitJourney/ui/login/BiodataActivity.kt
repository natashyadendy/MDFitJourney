package id.my.fitJourney.ui.login

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ActivityBiodataBinding
import id.my.fitJourney.util.BiodataSharePref

class BiodataActivity: AppCompatActivity() {
    private lateinit var binding: ActivityBiodataBinding
    private lateinit var genders: Array<Button>
    private var gendersString = arrayOf("male", "female")

    private var genderStatus: Int = 0;

    companion object{
        lateinit var instance: BiodataActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiodataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instance = this

        genders = arrayOf(binding.btnMale, binding.btnFemale)

        binding.btnMale.setOnClickListener {
            genderStatus = 0;
            changeButtonStatus(genderStatus);
        }

        binding.btnFemale.setOnClickListener {
            genderStatus = 1;
            changeButtonStatus(genderStatus);
        }

        binding.btnNext.setOnClickListener {
            val biodataSharedPref = BiodataSharePref(this)
            biodataSharedPref.saveGender(gendersString[genderStatus])
            biodataSharedPref.saveAge(binding.etOld.editText!!.text.toString())
            biodataSharedPref.saveHeight(binding.etHeight.editText!!.text.toString())
            biodataSharedPref.saveWeight(binding.etWeight.editText!!.text.toString())
            startActivity(Intent(this@BiodataActivity, LevelActivity::class.java));
        }
    }

    fun changeButtonStatus(index: Int){
        for(item in genders){
            item.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#838383"))
        }
        genders[index].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary))
    }
}