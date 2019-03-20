package com.otus.alexshr.validation_support;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;

/**
 * Created by alexshr
 * <p>
 * This scroll container is like a form for any amount of ValidTextInputLayout inside (on any deep).
 * The form is valid  if all fields are valid and subscribers receive info about form validation status.
 * It could be used eg to control  "Save" button state and so on
 */
public class ValidatedInputForm extends FrameLayout {

    Integer submitId;

    private LiveData<Boolean> validationLiveData;

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

        if (a.hasValue(R.styleable.ValidatedInputForm_submit_id)) {
            submitId = a.getResourceId(R.styleable.ValidatedInputForm_submit_id, 0);
        }
        a.recycle();
    }

    private static <T extends View> ArrayList<T> getViewsByType(ViewGroup root, Class<T> tClass) {
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

    /*public static Button findSubmitButton(ViewGroup root, String tag) {
        View view = root.findViewWithTag(tag);
        if (view instanceof Button) {
            return (Button) view;
        } else {
            final int childCount = root.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = root.getChildAt(i);
                if (child instanceof ViewGroup) {
                    return findSubmitButton((ViewGroup) child, tag);
                }
            }
            return null;
        }
    }*/

    /*private static Button findSubmitButton(ViewGroup root, String tag) {
        Timber.d("started root: %s",root);
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                return findSubmitButton((ViewGroup) child, tag);
            } else {
                Timber.d("child: %s",child);
                if (child.getTag() != null && child.getTag().equals(tag) && child instanceof Button) {
                    return (Button) child;
                }
            }
        }
        return null;
    }*/

    private static ArrayList<View> getViewsByTag(ViewGroup root, String tag) {
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }
        }
        return views;
    }

    private Button findSubmitBtn() {

        if (submitId != null) {
            View view = findViewById(submitId);
            if (view instanceof Button) return (Button) view;
        }
        return null;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) return;

        List<ValidatedTextInputLayout> inputLayouts = getViewsByType(this, ValidatedTextInputLayout.class);

        List<Observable<Boolean>> list = new ArrayList<>();

        for (ValidatedTextInputLayout input : inputLayouts) {
            list.add(input.getValidationObservable());
        }

        Observable<Boolean> validationObservable = Observable.combineLatest(list, results -> {
            for (Object res : results) {
                if (!(Boolean) res) return false;
            }
            return true;
        });

        validationLiveData = LiveDataReactiveStreams.fromPublisher(validationObservable.toFlowable(BackpressureStrategy.LATEST));

        Button button = findSubmitBtn();
        if (button != null) validationObservable.subscribe(button::setEnabled);
    }

    public LiveData<Boolean> getValidationLiveData() {
        return validationLiveData;
    }
}
