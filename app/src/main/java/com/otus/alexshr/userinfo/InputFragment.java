package com.otus.alexshr.userinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private MainViewModel mViewModel;
    private Unbinder unbinder;

    @OnClick(R.id.okBtn)
    void submit() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        validatedForm.addValidationListener(okBtn::setEnabled);
        validatedForm.validate();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
