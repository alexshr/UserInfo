package com.otus.alexshr.userinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otus.alexshr.userinfo.databinding.DisplayFragmentBinding;
import com.otus.alexshr.userinfo.di.Injectable;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

/**
 * Created by alexshr on 09.01.2019.
 */
public class DisplayFragment extends Fragment implements Injectable {

    @Inject
    ActivityViewModel activityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.display_title);

        DisplayFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.display_fragment,
                container, false);

        binding.setViewModel(activityViewModel);

        return binding.getRoot();
    }
}
