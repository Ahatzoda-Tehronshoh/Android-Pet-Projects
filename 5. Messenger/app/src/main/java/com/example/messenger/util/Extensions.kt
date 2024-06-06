package com.example.messenger.data

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.messenger.model.Contact

fun Context.getPhoneNumber(): String {
    return if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        )
        == PackageManager.PERMISSION_GRANTED
    ) {
        val telemamanger = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            telemamanger.line1Number ?: "+992 92 746 1110"
        } catch(e: Exception) {
            "+992 92 746 1110"
        }
    } else ""
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.fetchAllContacts(): List<Contact> {
    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )
        .use { cursor ->
            if (cursor == null) return emptyList()
            val builder = ArrayList<Contact>()
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME) ?: 0)
                        ?: "N/A"
                val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) ?: 0)
                        ?: "N/A"
                val icon =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI) ?: 0)
                        ?: "N/A"

                builder.add(Contact(name, phoneNumber, icon))
            }
            return builder
        }
}