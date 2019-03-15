package com.otus.alexshr.userinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otus.alexshr.userinfo.databinding.InputFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class InputFragment extends Fragment {

    private ActivityViewModel activityViewModel;

    private Navigator nav;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.input_title);

        InputFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.input_fragment,
                container, false);
        binding.setLifecycleOwner(this);

        activityViewModel = ViewModelProviders.of(getActivity()).get(ActivityViewModel.class);

        //for back stack
        if (savedInstanceState == null && activityViewModel.getUser() != null) {
            binding.nameInput.setText(activityViewModel.getUser().getName());
            binding.emailInput.setText(activityViewModel.getUser().getEmail());
            binding.phoneInput.setText(activityViewModel.getUser().getPhone());
        }

        nav = (Navigator) getActivity();

        binding.okBtn.setOnClickListener(btn -> {
            activityViewModel.setUser(new User(binding.nameInput.getText(), binding.emailInput.getText(), binding.phoneInput.getText()));

            nav.showDisplayFragment();
        });

        return binding.getRoot();
    }
}
