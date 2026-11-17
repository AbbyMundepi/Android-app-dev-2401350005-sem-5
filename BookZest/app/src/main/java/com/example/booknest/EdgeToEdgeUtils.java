package com.example.booknest;

import android.app.Activity;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Utility class for managing Android system bar insets, status bar padding,
 * display cutouts/notches, and navigation bar safe areas in BookNest.
 */
public class EdgeToEdgeUtils {

    /**
     * Applies status bar / display cutout padding and bottom navigation bar padding safely.
     *
     * @param activity   Target Activity instance
     * @param rootLayout Root view container
     * @param topView    Top toolbar / header view receiving top inset padding
     * @param bottomView Bottom navigation bar receiving bottom inset padding
     */
    public static void applyEdgeToEdge(final Activity activity, final View rootLayout, final View topView, final View bottomView) {
        if (activity == null || rootLayout == null) return;

        Window window = activity.getWindow();
        window.setBackgroundDrawableResource(R.color.background);
        WindowCompat.setDecorFitsSystemWindows(window, true);

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout, new OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(
                        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout()
                );

                if (topView != null) {
                    topView.setPadding(
                            topView.getPaddingLeft(),
                            systemBars.top,
                            topView.getPaddingRight(),
                            topView.getPaddingBottom()
                    );
                }

                if (bottomView != null) {
                    bottomView.setPadding(
                            bottomView.getPaddingLeft(),
                            bottomView.getPaddingTop(),
                            bottomView.getPaddingRight(),
                            systemBars.bottom
                    );
                }

                return insets;
            }
        });
    }
}
