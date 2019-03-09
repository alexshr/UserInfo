package com.otus.alexshr.validation_support;

import android.content.Context;
import android.content.res.TypedArray;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import androidx.core.util.Consumer;
import io.reactivex.Observable;
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

    private boolean isPhoneSDKPattern() {
        return validationPattern == null && getEditText().getInputType() == InputType.TYPE_CLASS_PHONE;
    }

    private boolean isEmailSDKPattern() {
        return validationPattern == null && (getEditText().getInputType() & InputType.TYPE_MASK_VARIATION) == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) return;

        helperText = getHelperText() + "";
        setHelperTextEnabled(false);

        if (isEmailSDKPattern()) {
            validationPattern = Patterns.EMAIL_ADDRESS;
        }

        if (isPhoneSDKPattern()) {
            getEditText().addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        }

        //validate(getEditText().getText().toString());//внимание на первую проверку!!!!
    }

    private Editable addPhonePrefixIfNeeded(Editable ed) {
        if (isPhoneSDKPattern() && isPhoneSDKPattern() && ed.length() > 0 && ed.charAt(0) != '+')
            ed.insert(0, "+");
        return ed;
    }

    public Observable<Boolean> getValidationObservable() {

        return RxTextView.afterTextChangeEvents(getEditText())
                .map(TextViewAfterTextChangeEvent::editable)
                .map(this::addPhonePrefixIfNeeded)
                .map(Editable::toString)
                .map(this::check)
                .doOnNext(this::setError);
    }

    private void setError(boolean isValid) {
        setError(isValid ? null : helperText);
    }

    public boolean check(String str) {
        boolean isValid = true;
        if (getEditText().getInputType() == InputType.TYPE_CLASS_PHONE) {
            str = str.trim().replaceAll("[^+0-9]", "");

            isValid = validationPattern == null ?
                    PhoneNumberUtil.getInstance().isPossibleNumber(str, Locale.getDefault().getCountry()) :
                    validationPattern.matcher(str).matches();
        } else if (validationPattern != null) {
            isValid = validationPattern.matcher(str).matches();
        }
        Timber.d("hint=%s, str=%s; isValid=%s; country=%s", getHint(), str, isValid, Locale.getDefault().getCountry());

        return isValid;
    }


    public String getText() {
        return getEditText().getText().toString();
    }

    public void setText(String text) {
        getEditText().setText(text);
    }
}
