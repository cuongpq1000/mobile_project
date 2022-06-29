package com.example.myapplication

import android.icu.text.StringPrepParseException

class Data {
    var item: String = ""
    var date: String = ""
    var id: String = ""
    var amount: Int = 0
    var month: Int = 0

    constructor(){

    }

    constructor(item: String, date: String, id: String, amount: Int, month: Int) {
        this.item = item
        this.date = date
        this.id = id
        this.amount = amount
        this.month = month
    }
    @JvmName("getAmount1")
    fun getAmount(): Int{
        return this.amount
    }

    @JvmName("getDate1")
    fun getDate(): String{
        return this.date
    }

    @JvmName("getItem1")
    fun getItem(): String{
        return this.item
    }

    @JvmName("getMonth1")
    fun getMonth(): Int{
        return this.month
    }


}