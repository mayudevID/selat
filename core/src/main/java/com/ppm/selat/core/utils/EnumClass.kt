package com.ppm.selat

import android.content.Intent

enum class Manufacturer {
    ALL,
    TOYOTA,
    HYUNDAI,
    SUZUKI,
    NISSAN,
    HONDA,
}

enum class TypeCar {
    ALL,
    SEDAN,
    SUV,
}

inline fun <reified T : Enum<T>> Intent.putExtra(victim: T): Intent =
    putExtra(T::class.java.name, victim.ordinal)

inline fun <reified T: Enum<T>> Intent.getEnumExtra(): T? =
    getIntExtra(T::class.java.name, -1)
        .takeUnless { it == -1 }
        ?.let { T::class.java.enumConstants?.get(it) }