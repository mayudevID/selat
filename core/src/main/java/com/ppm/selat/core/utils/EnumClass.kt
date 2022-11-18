package com.ppm.selat.core.utils

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

enum class TypeDataEdit {
    NAME,
    PDOB,
    EMAIL,
    PHONE,
    JOB,
    ADDRESS,
}

enum class Region {
    PROVINCE,
    DISTRICT,
    REGENCY,
    VILLAGE,
}

inline fun <reified T : Enum<T>> Intent.putExtra(victim: T): Intent =
    putExtra(T::class.java.name, victim.ordinal)

inline fun <reified T: Enum<T>> Intent.getEnumExtra(): T? =
    getIntExtra(T::class.java.name, -1)
        .takeUnless { it == -1 }
        ?.let { T::class.java.enumConstants?.get(it) }