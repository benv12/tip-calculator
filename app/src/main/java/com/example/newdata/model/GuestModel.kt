package com.example.newdata.model

class GuestModel(
    var guestName: String
) {
    // Other properties and methods

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GuestModel

        if (guestName != other.guestName) return false

        return true
    }

    override fun hashCode(): Int {
        return guestName.hashCode()
    }
}
