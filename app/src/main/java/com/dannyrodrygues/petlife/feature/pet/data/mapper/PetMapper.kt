package com.dannyrodrygues.petlife.feature.pet.data.mapper

import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import com.dannyrodrygues.petlife.feature.pet.data.remote.PetDto
import com.dannyrodrygues.petlife.feature.pet.data.remote.PetInsertDto
import com.dannyrodrygues.petlife.feature.pet.data.remote.PetUpdateDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val localDateFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

private val remoteDateFormatter =
    DateTimeFormatter.ISO_LOCAL_DATE

fun PetEntity.toInsertDto(): PetInsertDto {
    return PetInsertDto(
        tenantId = tenantId,
        name = name,
        species = species,
        breed = breed,
        gender = gender,
        birthDate = birthDate.toRemoteDate(),
        weight = weight,
        observations = observations,

        /*
         * A foto ainda é somente local.
         * O upload para Supabase Storage
         * será implementado depois.
         */
        photoPath = null,
    )
}

fun PetEntity.toUpdateDto(): PetUpdateDto {
    return PetUpdateDto(
        name = name,
        species = species,
        breed = breed,
        gender = gender,
        birthDate = birthDate.toRemoteDate(),
        weight = weight,
        observations = observations,

        /*
         * Upload de foto ainda não foi implementado.
         */
        photoPath = null,
    )
}

fun PetDto.toEntity(): PetEntity {
    return PetEntity(
        tenantId = tenantId,
        remoteId = id,
        name = name,
        species = species,
        breed = breed,
        gender = gender,
        birthDate = birthDate.toLocalDateFormat(),
        weight = weight,
        observations = observations,

        /*
         * photo_path remoto ainda não é
         * convertido para uma URI local.
         */
        photoUri = null,
    )
}

private fun String?.toRemoteDate(): String? {
    if (this.isNullOrBlank()) {
        return null
    }

    return runCatching {
        LocalDate
            .parse(this, localDateFormatter)
            .format(remoteDateFormatter)
    }.getOrNull()
}

private fun String?.toLocalDateFormat(): String? {
    if (this.isNullOrBlank()) {
        return null
    }

    return runCatching {
        LocalDate
            .parse(this, remoteDateFormatter)
            .format(localDateFormatter)
    }.getOrNull()
}