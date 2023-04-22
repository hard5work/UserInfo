package com.anish.app.users.utils.layouts

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import com.anish.app.users.R
import com.anish.app.users.utils.interfaces.BottomSheetInterface
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetLayout(
    private val layout: Int,
    private val onButtonClicked: BottomSheetInterface
) :
    BottomSheetDialogFragment(),
    View.OnClickListener {

    private val TAG = "BottomSheetLayout"
    private lateinit var requestAssistanceBtn: RelativeLayout

    /* companion object {
         fun newInstance(): BottomSheetLayout {

             return BottomSheetLayout()
         }
     }*/


    private var mBehavior: BottomSheetBehavior<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view = View.inflate(context, layout, null)


        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)

        onButtonClicked.onViewCreated(view)
        return dialog
    }


    override fun onClick(v: View?) {
        onButtonClicked.onClicked()
        mBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onStart() {
        super.onStart()
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    fun onCancelled(dialog: DialogInterface) {
        onCancel(dialog)
    }
}