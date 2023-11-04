package br.com.udesc.eso.tcc.studytalk.di

import android.content.Context
import br.com.udesc.eso.tcc.studytalk.core.presentation.activity.initial.InitialActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {

    fun inject(initialActivity: InitialActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

}