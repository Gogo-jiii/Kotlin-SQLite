package com.example.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(
    context: Context?, name: String?,
    factory: CursorFactory?, version: Int
) : SQLiteOpenHelper(context, name, null, version) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = "create table users (id integer primary key, name text)"
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val query = "DROP TABLE IF EXISTS users"
        db.execSQL(query)
        onCreate(db)
    }

    fun insert(name: String?): Boolean {
        val database = this.writableDatabase
        val values = ContentValues()
        values.put("name", name)
        database.insert(USERS_TABLE_NAME, null, values)
        database.close()
        return true
    }

    fun getUser(id: Int): UsersModel {
        val database = this.readableDatabase
        val cursor = database.query(
            USERS_TABLE_NAME,
            arrayOf(
                USERS_COLUMN_ID,
                USERS_COLUMN_NAME
            ),
            USERS_COLUMN_ID + "=?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        return UsersModel(
            cursor!!.getString(0).toInt(),
            cursor.getString(1)
        )
    }

    val allUsers: List<UsersModel?>
        get() {
            val list: MutableList<UsersModel?> = ArrayList()
            val database = this.readableDatabase
            val cursor = database.rawQuery("select * from users", null)
            if (cursor.moveToFirst()) {
                do {
                    val model = UsersModel()
                    model.iD = cursor.getString(0).toInt()
                    model.name = cursor.getString(1)
                    list.add(model)
                } while (cursor.moveToNext())
            }
            return list
        }
    val numberOfRows: Int
        get() {
            val database = this.readableDatabase
            return DatabaseUtils.queryNumEntries(database, USERS_TABLE_NAME).toInt()
        }

    fun update(id: Int, name: String?): Boolean {
        val database = this.writableDatabase
        val values = ContentValues()
        values.put("name", name)
        database.update(USERS_TABLE_NAME, values, "id = ? ", arrayOf(Integer.toString(id)))
        return true
    }

    fun delete(id: Int): Int {
        val database = this.writableDatabase
        return database.delete(USERS_TABLE_NAME, "id = ? ", arrayOf(Integer.toString(id)))
    }

    companion object {
        const val DATABASE_NAME = "MyDB.db"
        const val USERS_TABLE_NAME = "users"
        const val USERS_COLUMN_ID = "id"
        const val USERS_COLUMN_NAME = "name"
        var VERSION = 1
    }
}