package org.careerseekers.cseventsservice.enums

enum class QueueStatus(private val alias: String) {
    PARTICIPATES("Участвует"),
    IN_QUEUE("В очереди");

    companion object {
        fun QueueStatus.getAlias() = this.alias
    }
}