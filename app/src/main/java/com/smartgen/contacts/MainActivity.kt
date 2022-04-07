package com.smartgen.contacts

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.CursorAdapter
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private val cols = listOf<String>(ContactsContract.CommonDataKinds.Phone._ID,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER).toTypedArray()

    lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)


        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                102)
        } else {
            readContacts()
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == 102 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readContacts()
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @SuppressLint("Range")
    private fun readContacts() {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val rs = this.contentResolver.query(uri,
            cols,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val from = listOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER).toTypedArray()
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        val adapter =
            SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, rs, from, to, 0)
        listView.adapter = adapter


        if (rs != null) while (rs.moveToNext()) {
            var contactId = rs.getString(rs.getColumnIndex(ContactsContract.Contacts._ID))
            var name = rs.getString(rs.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            var number = rs.getString(rs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            Log.d("name", "readContacts: $name")
        }
    }

}