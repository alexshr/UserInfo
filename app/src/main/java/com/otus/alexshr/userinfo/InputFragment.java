package com.otus.alexshr.userinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class InputFragment extends Fragment {

    @BindView(R.id.okBtn)
    Button okBtn;
    @BindView(R.id.nameInput)
    ValidatedTextInputLayout nameInput;
    @BindView(R.id.emailInput)
    ValidatedTextInputLayout emailInput;
    @BindView(R.id.phoneInput)
    ValidatedTextInputLayout phoneInput;
    @BindView(R.id.materialCardView)
    MaterialCardView materialCardView;
    @BindView(R.id.validatedForm)
    ValidatedFrameLayout validatedForm;

    private MainViewModel viewModel;

    private Unbinder unbinder;

    private Manager manager;

    @OnClick(R.id.okBtn)
    void submit() {
        viewModel.setUser(new User(nameInput.getText(), emailInput.getText(), phoneInput.getText()));
        manager.showDisplayFragment();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.input_title);

        View view = inflater.inflate(R.layout.input_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        User user = viewModel.getUser();

        //for back stack
        if (user != null) {
            nameInput.setText(user.getName());
            emailInput.setText(user.getEmail());
            phoneInput.setText(user.getPhone());
        }

        manager = (Manager) getActivity();

        validatedForm.addValidationListener(okBtn::setEnabled);
        validatedForm.validate();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
