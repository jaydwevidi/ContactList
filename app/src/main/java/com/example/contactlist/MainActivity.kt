package com.example.contactlist

import android.Manifest
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import android.content.pm.PackageManager


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val requiredPermission = Manifest.permission.READ_CONTACTS
        val checkVal: Int = applicationContext.checkCallingOrSelfPermission(requiredPermission)

        if (checkVal==PackageManager.PERMISSION_GRANTED){
            displayContacts()
        }
        else{
            getpermission()
        }


    }
    fun getpermission(){
        val listener= object : PermissionListener{
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
                displayContacts()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }

        }
        Dexter.withContext(this)
            .withPermission(android.Manifest.permission.READ_CONTACTS)
            .withListener(listener)
            .check()
    }

    fun displayContacts(){
        val contacts = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI ,
            null , null , null , null
        )
        val sb = StringBuilder()
        if (contacts != null) {
            while (contacts.moveToNext()){
                val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val id = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))

                //Log.e("contacts : ", "$name , $number $id")
                sb.append("$id : $name : $number \n")
            }
            findViewById<TextView>(R.id.tvmy).text = sb.toString()
        }
    }
}