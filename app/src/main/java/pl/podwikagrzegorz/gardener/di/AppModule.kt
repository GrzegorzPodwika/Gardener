package pl.podwikagrzegorz.gardener.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import pl.podwikagrzegorz.gardener.data.domain.FirebaseSource
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseSource() : FirebaseSource = FirebaseSource()

/*    @Singleton
    @Provides
    fun provideUserRepository(firebaseSource : FirebaseSource) : UserRepository = UserRepository(firebaseSource)*/
}