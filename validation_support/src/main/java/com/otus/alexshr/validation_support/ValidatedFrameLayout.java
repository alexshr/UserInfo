package com.otus.alexshr.validation_support;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.core.util.Consumer;
import timber.log.Timber;

/**
 * Created by alexshr
 *
 * This container is like a form for any amount of ValidTextInputLayout inside (on any deep).
 * The form is valid  if all fields are valid and subscribers receive info about form validation status.
 * It could be used eg to control  "Save" button state and so on
 */
public class ValidatedFrameLayout extends FrameLayout {

    private boolean isAutoValidated;

    private Boolean isValid;

    private List<Consumer<Boolean>> validationListeners = new ArrayList<>();

    private List<ValidatedTextInputLayout> inputLayouts;

    public ValidatedFrameLayout(Context context) {
        this(context, null, 0);
    }

    public ValidatedFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ValidatedFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode()) return; //for layout editor

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ValidatedFrameLayout, 0, 0);

        isAutoValidated = a.getBoolean(R.styleable.ValidatedFrameLayout_auto_validation, true);
        a.recycle();
    }

    public static <T extends View> ArrayList<T> getViewsByType(ViewGroup root, Class<T> tClass) {
        final ArrayList<T> result = new ArrayList<>();
        int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup)
                result.addAll(getViewsByType((ViewGroup) child, tClass));

            if (tClass.isInstance(child))
                result.add(tClass.cast(child));
        }
        return result;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) return;

        inputLayouts = getViewsByType(this, ValidatedTextInputLayout.class);

        if (isAutoValidated) {
            for (ValidatedTextInputLayout input : inputLayouts) {
                input.addValidationListener(this::validate);
            }
        }
    }

    //call from ValidatedTextInputLayout
    public void validate(ValidatedTextInputLayout sourceInput) {
        Boolean isValidBefore = isValid;
        isValid = sourceInput == null || sourceInput.isValid();

        for (ValidatedTextInputLayout input : inputLayouts) {
            if (!isValid) break;
            isValid = input.isValid();

            Timber.d("checking input (hint=%s) isValid=%s", input.getHint(), input.isValid());
        }

        Timber.d("isValidBefore=%s; isValid=%s", isValidBefore, isValid);
        if (isValidBefore == null || isValid != isValidBefore) {
            for (Consumer<Boolean> listener : validationListeners) listener.accept(isValid);
        }
    }

    //call from outside
    public void validate() {
        validate(null);
    }


    public boolean isValid() {
        return isValid;
    }

    public void addValidationListener(Consumer<Boolean> listener) {
        validationListeners.add(listener);
    }

    public void removeValidationListener(Consumer<Boolean> listener) {
        validationListeners.remove(listener);
    }
}
