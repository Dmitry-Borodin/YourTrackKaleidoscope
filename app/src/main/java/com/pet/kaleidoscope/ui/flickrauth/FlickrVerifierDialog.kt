package com.pet.kaleidoscope.ui.flickrauth

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.pet.kaleidoscope.R
import com.viewbinder.bindView

/**
 * @author Dmitry Borodin on 3/14/19.
 */
class FlickrVerifierDialog(context: Context, private val callback: FlickrAuthClientCallback) :
    Dialog(context)  {
    private val editText by bindView<EditText>(R.id.dialog_verifier_edittext)
    private val okButton by bindView<Button>(R.id.dialog_verifier_ok_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_auth_verifier)
        setCancelable(true)
        setTitle(context.getString(R.string.flickr_auth_title))
        okButton.setOnClickListener {
            callback.onSuccessIntercepted(editText.text.toString())
            dismiss()
        }
    }
}