package com.example.burgers.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AndroidRuntimeException

object Utils {
    fun getShuffledListOfLists(vararg lists: List<Any>?): List<Any> {
        val shuffledList: ArrayList<Any> = arrayListOf()
        lists.forEach {
            it?.let { shuffledList.addAll(it) }
        }
        return shuffledList.shuffled()
    }

    fun getNormalListOfLists(vararg lists: List<Any>?): List<Any> {
        val normalList: ArrayList<Any> = arrayListOf()
        lists.forEach {
            it?.let { normalList.addAll(it) }
        }
        return normalList.shuffled()
    }

    fun openHttpsUrl(context:Context,link:String):Boolean{
        if (!link.startsWith("https")) return false
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            context.startActivity(intent)
        }catch (e: AndroidRuntimeException){
            return true
        }
        return true
    }
}