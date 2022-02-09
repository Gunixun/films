package com.example.myapplication.ui.contacts

import android.os.Bundle
import android.view.View
import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentContactsBinding
import com.example.myapplication.model.Contact
import com.example.myapplication.ui.BaseFragment


const val REQUEST_CODE = 42

class ContactsFragment :
    BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private lateinit var adapter: ContactsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ContactsAdapter()
        binding.contactsList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.contactsList.adapter = adapter
        checkPermission()
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    createContactDialog(it)
                }
                else -> {
                    //Запрашиваем разрешение
                    requestPermission()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                // Проверяем, дано ли пользователем разрешение по нашему запросу
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    getContacts()
                } else {
                    // Поясните пользователю, что экран останется пустым, потому что доступ к контактам не предоставлен
                    context?.let { createContactDialog(it) }
                }
                return
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    private fun createContactDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Доступ к контактам")
            .setMessage("Доступ к конктактам нам не нужен, но все же лучше нам его дать")
            .setPositiveButton("Предоставить доступ") { _, _ ->
                requestPermission()
            }
            .setNegativeButton("Отказать") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun getContacts() {
        context?.let {
            // Получаем ContentResolver у контекста
            val contentResolver: ContentResolver = it.contentResolver
            // Отправляем запрос на получение контактов и получаем ответ в виде Cursor
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
//                HISTORY_URI,
                null,
                null,
                null,
//                null
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            val contacts: MutableList<Contact> = mutableListOf()
            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    // Переходим на позицию в Cursor
                    if (cursor.moveToPosition(i)) {
                        contacts.add(Contact(
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                        ))

                    }
                }
            }
            cursorWithContacts?.close()
            adapter.setData(contacts)
        }

    }

}