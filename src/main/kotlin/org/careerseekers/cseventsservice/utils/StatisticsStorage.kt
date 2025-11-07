package org.careerseekers.cseventsservice.utils

import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

object StatisticsStorage {
    var platformsCount: AtomicLong = AtomicLong(0)
        private set

    var verifiedPlatformsCount: AtomicLong = AtomicLong(0)
        private set

    var directionsCount: AtomicLong = AtomicLong(0)
        private set

    var directionsWithoutDocs: AtomicLong = AtomicLong(0)
        private set

    var directionDocsCount: AtomicLong = AtomicLong(0)
        private set

    var lastDocumentUpload: LocalDateTime? = null
        private set

    fun setPlatformsCount(platformsCount: Long) = apply { this.platformsCount.set(platformsCount) }
    fun setVerifiedPlatformsCount(platformsCount: Long) = apply { this.verifiedPlatformsCount.set(platformsCount) }

    fun setDirectionsCount(count: Long) = apply { this.directionsCount.set(count) }
    fun setDirectionsWithoutDocs(count: Long) = apply { this.directionsWithoutDocs.set(count) }

    fun setDirectionDocsCount(count: Long) = apply { this.directionDocsCount.set(count) }
    fun setLastDocumentUpload(lastDocumentUpload: LocalDateTime?) = apply { this.lastDocumentUpload = lastDocumentUpload }
}