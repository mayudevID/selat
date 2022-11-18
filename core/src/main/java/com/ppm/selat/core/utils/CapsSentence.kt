package com.ppm.selat.core.utils

import java.util.*

fun getCapsSentences(tagName: String): String? {
    val splits = tagName.lowercase(Locale.getDefault()).split(" ").toTypedArray()
    val sb = StringBuilder()
    for (i in splits.indices) {
        val eachWord = splits[i]
        if (i > 0 && eachWord.length > 0) {
            sb.append(" ")
        }
        val cap = (eachWord.substring(0, 1).uppercase(Locale.getDefault())
                + eachWord.substring(1))
        sb.append(cap)
    }
    return sb.toString()
}