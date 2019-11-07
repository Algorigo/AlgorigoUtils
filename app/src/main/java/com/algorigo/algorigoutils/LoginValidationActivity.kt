package com.algorigo.algorigoutils

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.algorigo.library.LoginValidateUtil
import kotlinx.android.synthetic.main.activity_login_validation.*

class LoginValidationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_validation)

        emailEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validate()
            }
        })
        clearEmailBtn.setOnClickListener {
            emailEdit.setText("")
        }
        passwordEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validate()
            }
        })
        clearPasswordBtn.setOnClickListener {
            passwordEdit.setText("")
        }
        validate()
    }

    private fun validate() {
        val email = emailEdit.text.toString()
        val password = passwordEdit.text.toString()

        val result = LoginValidateUtil.validate(email, password)
        when (result) {
            LoginValidateUtil.ValidationResult.LOGIN_VALIDATION_OK -> {
                validationResultView.setText("Validation OK")
                validationResultView.setTextColor(Color.GREEN)
            }
            LoginValidateUtil.ValidationResult.EMAIL_VALIDATION_FAIL -> {
                validationResultView.setText("Email is wrong")
                validationResultView.setTextColor(Color.RED)
            }
            LoginValidateUtil.ValidationResult.PASSWORD_VALIDATION_FAIL -> {
                validationResultView.setText("Password is wrong")
                validationResultView.setTextColor(Color.RED)
            }
        }
    }
}
