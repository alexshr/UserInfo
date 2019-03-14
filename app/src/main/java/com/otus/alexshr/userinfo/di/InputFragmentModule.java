package com.otus.alexshr.userinfo.di;

import com.otus.alexshr.userinfo.InputFragment;
import com.otus.alexshr.userinfo.InputViewModel;

import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
class InputFragmentModule {

    @Provides
    @InputFragmentScope
    InputViewModel provideInputViewModel(InputFragment fragment) {
        Timber.d("creating InputViewModel");
        return ViewModelProviders.of(fragment).get(InputViewModel.class);
    }
}
