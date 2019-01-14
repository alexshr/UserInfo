package com.otus.alexshr.userinfo;

import android.content.Context;
import android.content.res.TypedArray;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import androidx.core.util.Consumer;
import timber.log.Timber;

/**
 * Created by alexshr
 */
public class ValidatedTextInputLayout extends TextInputLayout {

    public static String REGEXP_PHONE = "\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$";
    public static String REGEXP_PHONE_RU = "^((\\+7|7|8)+([0-9]){10})$";
    public static String REGEXP_PHONE_US = "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$";

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

        /*TypedArray p = context.obtainStyledAttributes(attrs,
                R.styleable.TextInputLayout, 0, 0);*/

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ValidatedTextInputLayout, 0, 0);

        if (a.hasValue(R.styleable.ValidatedTextInputLayout_regexp)) {
            String regexp = a.getString(R.styleable.ValidatedTextInputLayout_regexp);
            validationPattern = Pattern.compile(regexp);
        }
        a.recycle();
    }

    //http://phoneregex.com/
    public static String getPhoneRegexp(boolean isLocalized) {

        String country = isLocalized ? Locale.getDefault().getCountry() : "";

        Timber.d("isLocalzed: %s, country: %s", isLocalized, country);

        switch (country) {
            case "RU":
                return REGEXP_PHONE_RU;
            case "US":
                return REGEXP_PHONE_US;
            default:
                return REGEXP_PHONE;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) return;

        helperText = getHelperText() + "";
        setHelperTextEnabled(false);

        if (validationPattern == null) {

            if ((getEditText().getInputType() & InputType.TYPE_MASK_VARIATION) == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
                validationPattern = Patterns.EMAIL_ADDRESS;
                //Timber.d("type: email");
            } else if (getEditText().getInputType() == InputType.TYPE_CLASS_PHONE) {
                validationPattern = Pattern.compile(getPhoneRegexp(false));

                getEditText().addTextChangedListener(new PhoneNumberFormattingTextWatcher());

                //adding "+" to phone number
                getEditText().addTextChangedListener(new TextSimplyWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        String text = s.toString();
                        if (text.length() > 0 && text.charAt(0) != '+') {
                            s.insert(0, "+");
                        }
                        //Timber.d("before: %s, after: %s", text, s);
                    }
                });
                //Timber.d("type: phone");
            }
        }
        //validation
        if (validationPattern != null) {
            getEditText().addTextChangedListener(new TextSimplyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString().trim();
                    validate(str);
                }
            });
        }

        validationListeners.add(input -> setError(input.isValid() ? null : helperText));

        validate(getEditText().getText().toString());
    }

    public void validate(String str) {

        Boolean isValidBefore = isValid;
        isValid = true;

        if (validationPattern != null) {
            if (getEditText().getInputType() == InputType.TYPE_CLASS_PHONE) {
                str = str.replaceAll("[^+0-9]", "");
            }

            isValid = validationPattern.matcher(str).matches();
        }

        Timber.d("hint=%s, str=%s, isValidBefore=%s; isValid=%s", getHint(), str, isValidBefore, isValid);

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
