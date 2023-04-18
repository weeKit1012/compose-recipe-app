package com.example.composerecipeapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.composerecipeapp.data.data_source.RADatabase
import com.example.composerecipeapp.data.repository.FirebaseRepositoryImpl
import com.example.composerecipeapp.data.repository.RecipeRepositoryImpl
import com.example.composerecipeapp.data.repository.UserRepositoryImpl
import com.example.composerecipeapp.domain.repository.FirebaseRepository
import com.example.composerecipeapp.domain.repository.RecipeRepository
import com.example.composerecipeapp.domain.repository.UserRepository
import com.example.composerecipeapp.domain.use_case.firebase.UploadImage
import com.example.composerecipeapp.domain.use_case.recipe.*
import com.example.composerecipeapp.domain.use_case.user.LoginUser
import com.example.composerecipeapp.domain.use_case.user.RegisterUser
import com.example.composerecipeapp.domain.use_case.user.UserUseCases
import com.example.composerecipeapp.presentation.util.TempRepo
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule
{
    @Provides
    fun provideAppContext(app: Application): Context
    {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideFirebaseInstance(

    ): FirebaseStorage
    {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(app: Application): RADatabase
    {
        return Room.databaseBuilder(
                app,
                RADatabase::class.java,
                "ra_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
            db: RADatabase
    ): UserRepository
    {
        return UserRepositoryImpl(
                dao = db.userDAO
        )
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(
            db: RADatabase,
            appContext: Context
    ): RecipeRepository
    {
        return RecipeRepositoryImpl(
                dao = db.recipeDAO,
                context = appContext
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(
            storage: FirebaseStorage
    ): FirebaseRepository
    {
        return FirebaseRepositoryImpl(storage)
    }

    @Provides
    @Singleton
    fun provideUserUseCases(
            repository: UserRepository
    ): UserUseCases
    {
        return UserUseCases(
                registerUser = RegisterUser(repository),
                loginUser = LoginUser(repository)
        )
    }

    @Provides
    @Singleton
    fun provideRecipeUseCases(
            repository: RecipeRepository
    ): RecipeUseCases
    {
        return RecipeUseCases(
                insertRecipe = InsertRecipe(repository),
                updateRecipe = UpdateRecipe(repository),
                deleteRecipe = DeleteRecipe(repository),
                getRecipes = GetRecipes(repository),
                getRecipeForId = GetRecipeForId(repository),
                getRecipeTypes = GetRecipeTypes(repository),
                getRecipeForType = GetRecipesForType(repository)
        )
    }

    @Provides
    @Singleton
    fun provideUploadImageUseCase(
            repository: FirebaseRepository
    ): UploadImage
    {
        return UploadImage(repository)
    }

    @CoroutineIO
    @Provides
    @Singleton
    fun provideIOCoroutineScope(): CoroutineScope
    {
        return CoroutineScope(Dispatchers.IO)
    }

    @CoroutineDefault
    @Provides
    @Singleton
    fun provideDefaultCoroutineScope(): CoroutineScope
    {
        return CoroutineScope(Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideTempRepo(): TempRepo
    {
        return TempRepo
    }
}

// Coroutine Scopes
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CoroutineIO

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CoroutineDefault