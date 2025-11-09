package org.careerseekers.cseventsservice.enums

enum class EventTypes(private val alias: String) {
    QUALIFIERS("Отборочный этап"),
    FINAL("Финальный этап"),
    MASTER_CLASS("Мастер класс");

    companion object {
        fun EventTypes.getAlias() = this.alias
    }
}