package com.otus.alexshr.validation_support;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import androidx.core.util.Consumer;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Created by alexshr
 * <p>
 * This scroll container is like a form for any amount of ValidTextInputLayout inside (on any deep).
 * The form is valid  if all fields are valid and subscribers receive info about form validation status.
 * It could be used eg to control  "Save" button state and so on
 */
public class ValidatedInputForm extends ScrollView {

    private boolean isAutoValidated;

    private Boolean isValid;

    private List<Consumer<Boolean>> validationListeners = new ArrayList<>();

    private List<ValidatedTextInputLayout> inputLayouts;

    public ValidatedInputForm(Context context) {
        this(context, null, 0);
    }

    public ValidatedInputForm(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ValidatedInputForm(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode()) return; //for layout editor

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ValidatedInputForm, 0, 0);

        isAutoValidated = a.getBoolean(R.styleable.ValidatedInputForm_auto_validation, true);
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

            List<Observable<Boolean>> list = new ArrayList<Observable<Boolean>>();

            for (ValidatedTextInputLayout input : inputLayouts) {
                list.add(input.getValidationObservable());
            }

            Observable.combineLatest(list, results -> {
                for (Object res : results) {
                    if (!(Boolean) res) return false;
                }
                return true;
            }).subscribe(isValid -> Timber.d("isValid=%s", isValid));


        }
    }



}
