package com.otus.alexshr.userinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otus.alexshr.userinfo.databinding.InputFragmentBinding;
import com.otus.alexshr.userinfo.di.Injectable;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class InputFragment extends Fragment implements Injectable {

    @Inject
    ActivityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.input_title);

        InputFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.input_fragment,
                container, false);
        binding.setLifecycleOwner(getActivity());

        binding.setModel(viewModel);

        if (savedInstanceState != null) viewModel.getUser().restoreFrom(savedInstanceState);

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.getUser().saveTo(outState);
    }
}
