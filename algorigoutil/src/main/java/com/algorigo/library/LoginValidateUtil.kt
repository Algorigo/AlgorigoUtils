package com.algorigo.library

import java.util.regex.Pattern

/**
 * Created by jaehongyoo on 2018. 3. 12..
 */
object LoginValidateUtil {

    enum class ValidationResult {
        LOGIN_VALIDATION_OK,
        EMAIL_VALIDATION_FAIL,
        PASSWORD_VALIDATION_FAIL
    }

    // 이메일정규식
    private val VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    //비밀번호정규식
    private val VALID_PASSWOLD_REGEX_ALPHA_NUM = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$") // 4자리 ~ 16자리까지 가능

    fun validate(email: String, passwd: String): ValidationResult {
        if (!validateEmail(email))
            return ValidationResult.EMAIL_VALIDATION_FAIL

        return if (!validatePassword(passwd)) ValidationResult.PASSWORD_VALIDATION_FAIL else ValidationResult.LOGIN_VALIDATION_OK
    }

    fun validateEmail(emailStr: String): Boolean {
        val matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
        return matcher.find()
    }

    fun validatePassword(pwStr: String): Boolean {
        val matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr)
        return matcher.matches()
    }
}
