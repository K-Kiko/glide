package com.bumptech.glide.load.resource.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;

/**
 * Handles decoding Drawables with the v7 support library if present and falling back to the v4
 * support library otherwise.
 */
public final class DrawableDecoderCompat {
  private DrawableDecoderCompat() {
    // Utility class.
  }

  /**
   * See {@code getDrawable(Context, int, Theme)}.
   */
  public static Drawable getDrawable(
      Context ourContext, Context targetContext, @DrawableRes int id) {
    return getDrawable(ourContext, targetContext, id, /*theme=*/ null);
  }

  /**
   * @param theme Used instead of the {@link Theme} returned from the given {@link Context} if
   *              non-null when loading the {@link Drawable}.
   */
  public static Drawable getDrawable(
      Context ourContext, @DrawableRes int id, @Nullable Theme theme) {
    return getDrawable(ourContext, ourContext, id, theme);
  }

  private static Drawable getDrawable(
      Context ourContext, Context targetContext, @DrawableRes int id, @Nullable Theme theme) {
    try {
      return ContextCompat.getDrawable(targetContext, id);
    } catch (Resources.NotFoundException e) {
      // Ignored, this can be thrown when drawable compat attempts to decode a canary resource. If
      // that decode attempt fails, we still want to try with the v4 ResourcesCompat below.
    }

    return loadDrawableV4(targetContext, id, theme != null ? theme : targetContext.getTheme());
  }

  private static Drawable loadDrawableV4(
      Context context, @DrawableRes int id, @Nullable Theme theme) {
    Resources resources = context.getResources();
    return ResourcesCompat.getDrawable(resources, id, theme);
  }
}
