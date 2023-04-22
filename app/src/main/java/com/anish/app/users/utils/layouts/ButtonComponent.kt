package com.anish.app.users.utils.layouts

import android.view.View
import com.anish.app.users.databinding.LayoutButtonComponentBinding

class ButtonComponent(private val bind: LayoutButtonComponentBinding) {

    fun onClicked(onClickListener: View.OnClickListener) {
        bind.mainContainer.setOnClickListener(onClickListener)
    }

    fun setText(text: String) {
        bind.buttonText.text = text
    }
}