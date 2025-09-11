package org.careerseekers.cseventsservice.enums

enum class FileTypes(private val alias: String) {
    AVATAR("Аватар"),
    SNILS("СНИЛС"),
    STUDYING_CERTIFICATE("Сертификат об обучении в ОУ"),
    ADDITIONAL_STUDYING_CERTIFICATE("Сертификат об обучении в ОУ доп образования"),
    CONSENT_TO_CHILD_PDP("Согласие на обработку персональных данных ребёнка"),
    CONSENT_TO_MENTOR_PDP("Согласие на обработку персональных данных куратора"),
    CONSENT_TO_TUTOR_PDP("Согласие на обработку персональных данных куратора"),
    CONSENT_TO_EXPERT_PDP("Согласие на обработку персональных данных эксперта"),
    BIRTH_CERTIFICATE("Свидетельство о рождении ребёнка"),
    DIRECTION_ICON("Иконка компетенции"),
    TASK("Конкурсное задание отборочного этапа"),
    CRITERIA("Критерии оценивания отборочного этапа"),
    STATEMENT("Итоговая ведомость отборочного этапа"),
    FINAL_TASK("Конкурсное задание финального этапа"),
    FINAL_CRITERIA("Критерии оценивания финального этапа"),
    FINAL_STATEMENT("Итоговая ведомость финального этапа"),
    DESCRIPTION("Описание компетенции");

    companion object {
        fun FileTypes.getAlias() = this.alias
    }
}