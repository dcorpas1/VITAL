package com.vital.app.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vital.app.data.repository.CatalogRepository
import com.vital.app.data.repository.PlanRepository
import com.vital.app.data.repository.ProgressRepository
import com.vital.app.data.repository.UserRepositoryImpl
import com.vital.app.domain.repository.UserRepository
import com.vital.app.domain.usecase.GeneratePlanUseCase
import com.vital.app.domain.usecase.ResetDayMealUseCase
import com.vital.app.domain.usecase.SwapMealUseCase
import com.vital.app.domain.usecase.VitalMotorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides @Singleton
    fun provideUserRepositoryImpl(
        firestore: FirebaseFirestore
    ): UserRepositoryImpl = UserRepositoryImpl(firestore)

    // La interfaz UserRepository también se resuelve con UserRepositoryImpl
    @Provides @Singleton
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides @Singleton
    fun provideCatalogRepository(
        firestore: FirebaseFirestore
    ): CatalogRepository = CatalogRepository(firestore)

    @Provides @Singleton
    fun providePlanRepository(
        firestore: FirebaseFirestore
    ): PlanRepository = PlanRepository(firestore)

    @Provides @Singleton
    fun provideProgressRepository(
        firestore: FirebaseFirestore
    ): ProgressRepository = ProgressRepository(firestore)

    @Provides @Singleton
    fun provideVitalMotorUseCase(): VitalMotorUseCase = VitalMotorUseCase()

    @Provides @Singleton
    fun provideGeneratePlanUseCase(
        catalogRepository: CatalogRepository
    ): GeneratePlanUseCase = GeneratePlanUseCase(catalogRepository)

    @Provides @Singleton
    fun provideResetDayMealUseCase(
        catalogRepository: CatalogRepository,
        planRepository: PlanRepository
    ): ResetDayMealUseCase = ResetDayMealUseCase(catalogRepository, planRepository)

    @Provides @Singleton
    fun provideSwapMealUseCase(
        catalogRepository: CatalogRepository,
        planRepository: PlanRepository
    ): SwapMealUseCase = SwapMealUseCase(catalogRepository, planRepository)
}
