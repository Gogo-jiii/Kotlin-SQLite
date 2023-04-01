package com.example.sqlite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlite.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var databaseManager: DatabaseManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnInsertData.setOnClickListener(this)
        binding.btnRetrieveData.setOnClickListener(this)
        binding.btnUpdateData.setOnClickListener(this)
        binding.btnDeleteData.setOnClickListener(this)

        databaseManager = DatabaseManager(
            this, DatabaseManager.DATABASE_NAME, null,
            DatabaseManager.VERSION
        )
    }

    override fun onClick(v: View?) {
        val rows: Int
        var id = -1

        when (v?.id) {
            R.id.btnInsertData -> {
                val nameToInsert: String = binding.tilInsert.editText?.text.toString()
                if (databaseManager!!.insert(nameToInsert)) {
                    Toast.makeText(this, "Data inserted.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Operation Failed!", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.btnRetrieveData -> {
                val users = databaseManager!!.allUsers
                Toast.makeText(this, users.toString(), Toast.LENGTH_SHORT).show()
            }

            R.id.btnUpdateData -> {
                val oldName: String = binding.tilDataToBeUpdated.editText?.text.toString()
                rows = databaseManager!!.numberOfRows

                for (i in 0 until rows) {
                    if (oldName == databaseManager!!.allUsers[i]!!.name) {
                        id = databaseManager!!.allUsers[i]!!.iD
                        break
                    }
                }
                val newName: String = binding.tilUpdatedData.editText?.text.toString()

                if (id != -1) {
                    if (databaseManager!!.update(id, newName)) {
                        Toast.makeText(this, "Data updated.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Operation Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.btnDeleteData -> {
                val nameToDelete: String = binding.tilDataToBeDeleted.editText?.text.toString()
                rows = databaseManager!!.numberOfRows

                for (i in 0 until rows) {
                    if (nameToDelete == databaseManager!!.allUsers[i]!!.name) {
                        id = databaseManager!!.allUsers[i]!!.iD
                        break
                    }
                }

                if (id != -1) {
                    if (databaseManager!!.delete(id) == 1) {
                        Toast.makeText(this, "Data deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Operation Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
}