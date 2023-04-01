package com.example.sqlite

class UsersModel {
    var iD = 0
    var name: String? = null

    constructor() {}
    constructor(ID: Int, name: String?) {
        iD = ID
        this.name = name
    }

    override fun toString(): String {
        return "ID=$iD, name='$name"
    }
}