package com.vsi.utils.validator

import android.widget.EditText
import com.vsi.R
import java.util.regex.Pattern

/**
 * Created by Paresh Devatval
 */

/**
 * A utility class which contains methods with all the validation logic of whole app.
 */
object Validator {

    private const val EMAIL_PATTERN: String = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z]{2,8}" +
            ")+"

    private const val PASSWORD_PATTERN: String =
        "^.*(?=.{6,20})(?=.*\\d)(?=.*[a-zA-Z])(^[a-zA-Z0-9._@!&$*%+-:/><#]+$)"


    fun validateEmail(email: String?): ValidationErrorModel? {
        return if (isBlank(email))
            ValidationErrorModel(R.string.blank_email, ValidationError.EMAIL)
        else if (!Pattern.compile(EMAIL_PATTERN).matcher(email).matches())
            ValidationErrorModel(R.string.invalid_email, ValidationError.EMAIL)
        else
            null
    }

    fun validateConfirmEmail(
        email: String?,
        confirmEmail: String
    ): ValidationErrorModel? {
        return if (isBlank(confirmEmail))
            ValidationErrorModel(R.string.blank_confirm_email, ValidationError.EMAIL)
        else if (!Pattern.compile(EMAIL_PATTERN).matcher(confirmEmail).matches())
            ValidationErrorModel(R.string.invalid_email, ValidationError.EMAIL)
        else if (email != confirmEmail)
            ValidationErrorModel(R.string.invalid_confirm_email, ValidationError.EMAIL)
        else
            null
    }


    fun validatePassword(password: String?, msg: Int): ValidationErrorModel? {
        return when {
            isBlank(password) -> ValidationErrorModel(msg, ValidationError.PASSWORD)
            password!!.length < 6 -> ValidationErrorModel(
                R.string.invalid_password
                , ValidationError.PASSWORD
            )
            /* else if (!Pattern.compile(PASSWORD_PATTERN).matcher(password).matches())
                 ValidationErrorModel(R.string.invalid_password, ValidationError.PASSWORD)*/
            else -> null
        }
    }

    fun validateNewPassword(password: String?, msg: Int): ValidationErrorModel? {
        return when {
            isBlank(password) -> ValidationErrorModel(msg, ValidationError.PASSWORD)
            password!!.length < 6 -> ValidationErrorModel(
                R.string.invalid_password
                , ValidationError.PASSWORD
            )
            /* else if (!Pattern.compile(PASSWORD_PATTERN).matcher(password).matches())
                 ValidationErrorModel(R.string.invalid_password, ValidationError.PASSWORD)*/
            else -> null
        }
    }

    fun validateConfirmPassword(
        password: String?,
        confirmPassword: String?
    ): ValidationErrorModel? {
        return when {
            isBlank(confirmPassword) -> ValidationErrorModel(
                R.string.blank_confirm_password,
                ValidationError.CONFIRM_PASSWORD
            )
            password != confirmPassword -> ValidationErrorModel(
                R.string.invalid_confirm_password,
                ValidationError.CONFIRM_PASSWORD
            )
            else -> null
        }
    }

    fun isBlank(text: String?): Boolean {
        return text == null || text.trim().isEmpty()
    }

    fun validateData(data: String): ValidationErrorModel? {
        return if (isBlank(data)) ValidationErrorModel(
            R.string.blank_data,
            ValidationError.DATA
        ) else null
    }

    /*fun validateUserName(userName: String?): ValidationErrorModel? {
        return if (isBlank(userName))
            ValidationErrorModel(R.string.blank_username, ValidationError.USERNAME)
        // else if (!Pattern.compile(EMAIL_PATTERN).matcher(userName).matches())
        //    ValidationErrorModel(R.string.invalid_email, ValidationError.USERNAME)
        else
            null
    }*/

    fun isBlank(editText: EditText): Boolean {
        return editText.text == null || editText.text.trim().isEmpty()
    }

    fun validateData(str: String, resourceId: Int): ValidationErrorModel? {
        return if (isBlank(str)) {
            ValidationErrorModel(resourceId, ValidationError.DATA)
        } else null
    }

    fun validateNumber(strNumber: String, min: Int, max: Int): Boolean {
        return strNumber.length in min..max
    }

    fun validateAllFields(data: ArrayList<String>): ValidationErrorModel? {
        for (str in data) {
            if (isBlank(str)) {
                return@validateAllFields ValidationErrorModel(
                    R.string.msg_all_field_required,
                    ValidationError.DATA
                )
            }
        }
        return null
    }
}