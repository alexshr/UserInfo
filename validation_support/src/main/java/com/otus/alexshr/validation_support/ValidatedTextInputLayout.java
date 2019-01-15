package com.otus.alexshr.validation_support;

import android.content.Context;
import android.content.res.TypedArray;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import androidx.core.util.Consumer;
import timber.log.Timber;

/**
 * Created by alexshr
 * <p>
 * TextInputLayout uses TextWatcher for validation, formatting, masking and so on
 * <p>
 * Use attr "regexp" if you need validation
 * <p>
 * Special behaviour for "phone" and "email" types without regexp:
 * - they are validated by google classes
 * - in addition phone numbers are masked and formatted by google PhoneNumberFormattingTextWatcher
 * and I add "+" for greater clarity
 * - Locale.getDefault().getCountry() is used for phone handling
 * <p>
 * TODO support validation by submitting (eg "ok" btn)
 */
public class ValidatedTextInputLayout extends TextInputLayout {

    //avoid unnecessary regexp compilation
    private Pattern validationPattern;

    private Boolean isValid;

    private String helperText = "";

    private List<Consumer<ValidatedTextInputLayout>> validationListeners = new ArrayList<>();

    public ValidatedTextInputLayout(Context context) {
        this(context, null, 0);
    }

    public ValidatedTextInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ValidatedTextInputLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode()) return; //for layout editor

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ValidatedTextInputLayout, 0, 0);

        if (a.hasValue(R.styleable.ValidatedTextInputLayout_regexp)) {
            String regexp = a.getString(R.styleable.ValidatedTextInputLayout_regexp);
            validationPattern = Pattern.compile(regexp);
        }
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) return;

        helperText = getHelperText() + "";
        setHelperTextEnabled(false);

        if (validationPattern == null && (getEditText().getInputType() & InputType.TYPE_MASK_VARIATION) == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
            validationPattern = Patterns.EMAIL_ADDRESS;
        }

        if (validationPattern == null && getEditText().getInputType() == InputType.TYPE_CLASS_PHONE) {
            getEditText().addTextChangedListener(new PhoneNumberFormattingTextWatcher());

            //adding "+" to phone number
            getEditText().addTextChangedListener(new TextSimplyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString();
                    if (text.length() > 0 && text.charAt(0) != '+') {
                        s.insert(0, "+");
                    }
                }
            });
        }

        //validation
        if (validationPattern != null || getEditText().getInputType() == InputType.TYPE_CLASS_PHONE) {
            getEditText().addTextChangedListener(new TextSimplyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString().trim();
                    validate(str);
                }
            });
        }

        validationListeners.add(input -> {
            setError(input.isValid() ? null : helperText);
        });

        validate(getEditText().getText().toString());
    }

    public void validate(String str) {

        Boolean isValidBefore = isValid;
        isValid = true;

        if (getEditText().getInputType() == InputType.TYPE_CLASS_PHONE) {
            str = str.replaceAll("[^+0-9]", "");

            isValid = validationPattern == null ?
                    PhoneNumberUtil.getInstance().isPossibleNumber(str, Locale.getDefault().getCountry()) :
                    validationPattern.matcher(str).matches();
        } else if (validationPattern != null) {
            isValid = validationPattern.matcher(str).matches();
        }

        Timber.d("hint=%s, str=%s, isValidBefore=%s; isValid=%s; country=%s", getHint(), str, isValidBefore, isValid, Locale.getDefault().getCountry());

        if (isValidBefore == null || isValid != isValidBefore) {
            for (Consumer<ValidatedTextInputLayout> listener : validationListeners)
                listener.accept(this);
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public void addValidationListener(Consumer<ValidatedTextInputLayout> validationListener) {
        validationListeners.add(validationListener);
    }

    public void removeValidationListener(Consumer<Boolean> validationListener) {
        validationListeners.remove(validationListener);
    }

    public String getText() {
        return getEditText().getText().toString();
    }

    public void setText(String text) {
        getEditText().setText(text);
    }

    private static class TextSimplyWatcher implements TextWatcher {

        private boolean isFormatting;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
