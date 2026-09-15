package sa.techlight.kitchen;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.widget.Button;
import android.widget.TextView;

final class Ui {
    static final int INK = Color.rgb(8, 11, 21);
    static final int SURFACE = Color.rgb(18, 22, 37);
    static final int SURFACE_2 = Color.rgb(29, 34, 54);
    static final int PURPLE = Color.rgb(126, 78, 255);
    static final int BRAND_BLUE = Color.rgb(58, 128, 255);
    static final int MINT = Color.rgb(30, 205, 158);
    static final int AMBER = Color.rgb(255, 178, 64);
    static final int DANGER = Color.rgb(247, 78, 108);
    static final int TEXT_MUTED = Color.rgb(157, 169, 198);

    private Ui() {
    }

    static int dp(Context context, float f) {
        return Math.round(f * context.getResources().getDisplayMetrics().density);
    }

    static GradientDrawable rounded(int i, float f, Context context) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(i);
        gradientDrawable.setCornerRadius(dp(context, f));
        return gradientDrawable;
    }

    static GradientDrawable outlined(int i, int i2, float f, Context context) {
        GradientDrawable gradientDrawableRounded = rounded(i, f, context);
        gradientDrawableRounded.setStroke(dp(context, 1.0f), i2);
        return gradientDrawableRounded;
    }

    static GradientDrawable outlined(int i, int i2, float f, float f2, Context context) {
        GradientDrawable gradientDrawableRounded = rounded(i, f2, context);
        gradientDrawableRounded.setStroke(dp(context, f), i2);
        return gradientDrawableRounded;
    }

    static GradientDrawable gradient(int i, int i2, float f, Context context) {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{i, i2});
        gradientDrawable.setCornerRadius(dp(context, f));
        return gradientDrawable;
    }

    static Drawable pressable(int i, float f, Context context) {
        return new RippleDrawable(ColorStateList.valueOf(Color.argb(70, 255, 255, 255)), rounded(i, f, context), null);
    }

    static TextView text(Context context, String str, float f, int i, boolean z) {
        TextView textView = new TextView(context);
        textView.setText(str);
        textView.setTextSize(f);
        textView.setTextColor(i);
        textView.setGravity(8388627);
        textView.setTypeface(Typeface.create("sans", z ? 1 : 0));
        textView.setTextDirection(1);
        return textView;
    }

    static Button button(Context context, String str, int i) {
        Button button = new Button(context);
        button.setText(str);
        button.setTextSize(16.0f);
        button.setTextColor(-1);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setGravity(17);
        button.setMinHeight(0);
        button.setMinWidth(0);
        button.setPadding(dp(context, 16.0f), 0, dp(context, 16.0f), 0);
        button.setBackground(pressable(i, 12.0f, context));
        return button;
    }
}
