package com.ppm.selat.core.utils

fun convertManufacturerToInt(manufacturer: Manufacturer): Int {
    return when (manufacturer) {
        Manufacturer.TOYOTA -> 1
        Manufacturer.HONDA -> 2
        Manufacturer.NISSAN -> 3
        Manufacturer.HYUNDAI -> 4
        Manufacturer.SUZUKI -> 5
        Manufacturer.ALL -> 0
    }
}

fun convertIntToManufacturer(manufacturer: Int): Manufacturer {
    return when (manufacturer) {
        1 -> Manufacturer.TOYOTA
        2 -> Manufacturer.HONDA
        3 -> Manufacturer.NISSAN
        4 -> Manufacturer.HYUNDAI
        5 -> Manufacturer.SUZUKI
        else -> Manufacturer.ALL

    }
}

fun convertStringToTypeCar(typeCar: String): TypeCar {
    return when (typeCar) {
        "SEDAN" -> TypeCar.SEDAN
        "SUV - MPV" -> TypeCar.SUV
        else -> TypeCar.ALL
    }
}

fun convertStringToManufacturer(data: String) : Manufacturer {
    return when (data) {
        "Toyota" -> Manufacturer.TOYOTA
        "Hyundai" -> Manufacturer.HYUNDAI
        "Suzuki" -> Manufacturer.SUZUKI
        "Nissan" -> Manufacturer.NISSAN
        "Honda" -> Manufacturer.HONDA
        else -> Manufacturer.ALL
    }
}

fun convertTypeCarToString(typeCar: TypeCar) : String {
    return when (typeCar) {
        TypeCar.SEDAN ->"SEDAN"
        TypeCar.SUV -> "SUV - MPV"
        TypeCar.ALL -> "ALL"
    }
}

fun convertManufacturerToString(manufacturer: Manufacturer): String {
    return when (manufacturer) {
        Manufacturer.TOYOTA -> "Toyota"
        Manufacturer.HONDA -> "Honda"
        Manufacturer.NISSAN -> "Nissan"
        Manufacturer.HYUNDAI -> "Hyundai"
        Manufacturer.SUZUKI -> "Suzuki"
        Manufacturer.ALL -> "All"
    }
}