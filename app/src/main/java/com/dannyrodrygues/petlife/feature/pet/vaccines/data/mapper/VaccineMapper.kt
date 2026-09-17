package com.dannyrodrygues.petlife.feature.pet.vaccines.data.mapper

import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineEntity
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.remote.VaccineDto
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.remote.VaccineInsertDto
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.remote.VaccineUpdateDto
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val remoteDateFormatter =
    DateTimeFormatter.ISO_LOCAL_DATE

fun VaccineEntity.toInsertDto(
    remotePetId: String,
): VaccineInsertDto {

    val applicationDate =
        requireNotNull(
            applicationDateMillis.toRemoteDate(),
        ) {
            "A data de aplicação da vacina é obrigatória."
        }

    return VaccineInsertDto(
        tenantId = tenantId,
        petId = remotePetId,
        name = name,
        doseDescription = doseDescription,
        applicationDate = applicationDate,
        nextDoseDate = nextDoseDateMillis.toRemoteDate(),
        observations = observations,
    )
}

fun VaccineEntity.toUpdateDto(): VaccineUpdateDto {

    val applicationDate =
        requireNotNull(
            applicationDateMillis.toRemoteDate(),
        ) {
            "A data de aplicação da vacina é obrigatória."
        }

    return VaccineUpdateDto(
        name = name,
        doseDescription = doseDescription,
        applicationDate = applicationDate,
        nextDoseDate = nextDoseDateMillis.toRemoteDate(),
        observations = observations,
    )
}

fun VaccineDto.toEntity(
    localPetId: Long,
): VaccineEntity {
    return VaccineEntity(
        tenantId = tenantId,
        petId = localPetId,
        remoteId = id,
        name = name,
        doseDescription = doseDescription,
        applicationDateMillis =
            applicationDate.toLocalDateMillis(),
        nextDoseDateMillis =
            nextDoseDate.toLocalDateMillis(),
        observations = observations,
        pendingSync = false,
        pendingDelete = false,
    )
}

private fun Long?.toRemoteDate(): String? {
    if (this == null) {
        return null
    }

    return runCatching {
        Instant
            .ofEpochMilli(this)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .format(remoteDateFormatter)
    }.getOrNull()
}

private fun String?.toLocalDateMillis(): Long? {
    if (this.isNullOrBlank()) {
        return null
    }

    return runCatching {
        LocalDate
            .parse(this, remoteDateFormatter)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }.getOrNull()
}