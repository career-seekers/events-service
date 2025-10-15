package org.careerseekers.cseventsservice.utils

import java.time.LocalDateTime

object StatisticsStorage {
    var platformsCount: Long = 0
        private set

    var verifiedPlatformsCount: Long = 0
        private set

    var directionsCount: Long = 0
        private set

    var directionsWithoutDocs: Long = 0
        private set

    var directionDocsCount: Long = 0
        private set

    var lastDocumentUpload: LocalDateTime? = null
        private set

    fun setPlatformsCount(platformsCount: Long) = apply { this.platformsCount = platformsCount }
    fun setVerifiedPlatformsCount(platformsCount: Long) = apply { this.verifiedPlatformsCount = platformsCount }

    fun setDirectionsCount(count: Long) = apply { this.directionsCount = count }
    fun setDirectionsWithoutDocs(count: Long) = apply { this.directionsWithoutDocs = count }

    fun setDirectionDocsCount(count: Long) = apply { this.directionDocsCount = count }
    fun setLastDocumentUpload(lastDocumentUpload: LocalDateTime?) = apply { this.lastDocumentUpload = lastDocumentUpload }
}