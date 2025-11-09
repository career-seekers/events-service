package org.careerseekers.cseventsservice.enums

enum class EventFormats(private val alias: String) {
    OFFLINE("Офлайн"),
    ONLINE("Онлайн");

    companion object {
        fun EventFormats.getAlias() = this.alias
    }
}