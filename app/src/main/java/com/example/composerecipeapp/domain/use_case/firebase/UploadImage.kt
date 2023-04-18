package com.example.composerecipeapp.domain.use_case.firebase

import android.net.Uri
import com.example.composerecipeapp.domain.repository.FirebaseRepository

class UploadImage(
        private val repository: FirebaseRepository
)
{
    suspend operator fun invoke(fileUri: Uri): String
    {
        return repository.uploadImageToStorage(fileUri)
    }
}