package org.careerseekers.cseventsservice.enums

enum class DirectionAgeCategory {
    PRESCHOOL_1,
    PRESCHOOL_2,
    SCHOOL_1,
    SCHOOL_2,
    SCHOOL_3;

    companion object {
        fun getAgeCategory(age: Int): DirectionAgeCategory {
            return when (age) {
                in 4..5 -> PRESCHOOL_1
                in 6..7 -> PRESCHOOL_2
                in 7..8 -> SCHOOL_1
                in 9..11 -> SCHOOL_2
                in 12..13 -> SCHOOL_3
                else -> throw IllegalArgumentException("This age not supported.")
            }
        }
    }
}