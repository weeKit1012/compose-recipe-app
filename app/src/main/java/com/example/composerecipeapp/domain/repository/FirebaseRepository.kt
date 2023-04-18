package com.example.composerecipeapp.domain.repository

import android.net.Uri

interface FirebaseRepository
{
    suspend fun uploadImageToStorage(fileUri: Uri): String
}