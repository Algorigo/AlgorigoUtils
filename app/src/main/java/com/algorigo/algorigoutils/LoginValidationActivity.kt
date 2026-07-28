package com.algorigo.algorigoutils

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.algorigo.algorigoutils.databinding.ActivityLoginValidationBinding
import com.algorigo.library.LoginValidateUtil

class LoginValidationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginValidationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginValidationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validate()
            }
        })
        binding.clearEmailBtn.setOnClickListener {
            binding.emailEdit.setText("")
        }
        binding.passwordEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validate()
            }
        })
        binding.clearPasswordBtn.setOnClickListener {
            binding.passwordEdit.setText("")
        }
        validate()
    }

    private fun validate() {
        val email = binding.emailEdit.text.toString()
        val password = binding.passwordEdit.text.toString()

        val result = LoginValidateUtil.validate(email, password)
        when (result) {
            LoginValidateUtil.ValidationResult.LOGIN_VALIDATION_OK -> {
                binding.validationResultView.setText("Validation OK")
                binding.validationResultView.setTextColor(Color.GREEN)
            }
            LoginValidateUtil.ValidationResult.EMAIL_VALIDATION_FAIL -> {
                binding.validationResultView.setText("Email is wrong")
                binding.validationResultView.setTextColor(Color.RED)
            }
            LoginValidateUtil.ValidationResult.PASSWORD_VALIDATION_FAIL -> {
                binding.validationResultView.setText("Password is wrong")
                binding.validationResultView.setTextColor(Color.RED)
            }
        }
    }
}
