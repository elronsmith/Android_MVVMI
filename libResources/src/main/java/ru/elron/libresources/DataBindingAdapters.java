package ru.elron.libresources;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

/**
 * содержит методы для дата биндинга
 */
public class DataBindingAdapters {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindingAdapter("app:vectorDrawable")
    public static void setVectorDrawable(ImageView view, @DrawableRes Integer resId) {
        view.setImageDrawable(VectorDrawableCompat.create(
                view.getResources(),
                resId,
                null));
    }

    @BindingAdapter("app:setBackgroundColor")
    public static void setBackgroundColor(View view, Integer color) {
        view.setBackgroundColor(color);
    }

    public static void setMenuItemVectorDrawable(MenuItem menuItem, Context context, Integer drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
        menuItem.setIcon(drawable);
    }

    @BindingAdapter({"app:visibleInvisible"})
    public static void viewVisibleInvisible(View view, boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter({"app:visibleGone"})
    public static void viewVisibleGone(View view, boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter({"app:onClick"})
    public static void viewOnClick(View view, View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }

    @BindingAdapter({"app:src"})
    public static void imageDrawable(ImageView imageView, Integer drawableRes) {
        imageView.setImageResource(drawableRes);
    }

}
