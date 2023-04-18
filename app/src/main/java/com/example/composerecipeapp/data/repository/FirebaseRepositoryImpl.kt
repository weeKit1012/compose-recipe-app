package com.example.composerecipeapp.data.repository

import android.net.Uri
import android.util.Log
import com.example.composerecipeapp.domain.repository.FirebaseRepository
import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseRepositoryImpl(
        private val _storage: FirebaseStorage
) : FirebaseRepository
{
    override suspend fun uploadImageToStorage(fileUri: Uri): String = suspendCoroutine { continuation ->
        val storageRef = _storage.reference
        val fileRef = storageRef.child("images/${fileUri.lastPathSegment}")
        val uploadTask = fileRef.putFile(fileUri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful)
            {
                task.exception?.let {
                    throw it
                }
            }
            fileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                continuation.resume(task.result.toString())
            }
            else
            {
                // Handle failures
                // ...
            }
        }.addOnFailureListener { ex ->
            Log.d("Exception", "${ex.message}")
            continuation.resumeWithException(ex)
        }
    }
}