package com.anish.app.users.utils.layouts

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import com.anish.app.users.databinding.LayoutInputFieldBinding
import com.anish.app.users.utils.DefaultInputType
import java.util.regex.Matcher
import java.util.regex.Pattern


class TextInputComponent(private val bind: LayoutInputFieldBinding) {

    fun setHint(hint: String) {
        bind.hintTextView.text = hint
        bind.inputEditField.hint = hint
    }

    private fun getHint(): String = bind.hintTextView.text.toString()

    fun setInputField(type: Int) {
        bind.inputEditField.inputType = type

    }

    fun getText(): String = bind.inputEditField.text.toString().trim()

    fun setText(text: String) {
        bind.inputEditField.setText(text)
        bind.inputEditField.setSelection(text.length)
    }

    private fun isEmpty(): Boolean = getText().isEmpty()

    fun isValid(): Boolean = if (isEmpty()) {
        setError(getHint() + " is required")
        false
    } else {
        setError(null)
        true
    }

    fun setError(error: String?) {
        bind.inputEditField.error = error
    }

    fun watcher(watcher: TextWatcher) {
        bind.inputEditField.addTextChangedListener(watcher)
    }

    val watchers = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            setError(null)
        }

    }


    fun setInputType(inputType: DefaultInputType?) {
        when (inputType) {
            DefaultInputType.Email -> {
                bind.inputEditField.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            DefaultInputType.FullName -> {
                bind.inputEditField.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            }
            DefaultInputType.Number -> {
                bind.inputEditField.inputType = InputType.TYPE_CLASS_NUMBER
            }
            DefaultInputType.CapSentence -> {
                bind.inputEditField.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            }
            else -> {
                bind.inputEditField.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            }
        }
    }

    fun isValidEmail(): Boolean {
        val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
        val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(getText())
        return matcher.matches()
    }

    fun isValidName(): Boolean {
        val nameReg = "^[a-zA-Z ,.'-]+$"
        val pattern = Pattern.compile(nameReg, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(getText())
        return matcher.matches()
    }

    fun isValidAddress(): Boolean {
        val regex = "^[a-zA-Z0-9 ,.'-()]+$"
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(getText())
        return matcher.matches()
    }
}