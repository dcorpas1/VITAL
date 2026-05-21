package com.vital.app.domain.repository

import com.vital.app.domain.model.UserProfile

interface UserRepository {
    suspend fun saveProfile(profile: UserProfile): Result<Unit>
    suspend fun getProfile(uid: String): Result<UserProfile>
}