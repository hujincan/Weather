package org.bubbble.weather.utils

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.annotations.NotNull

/**
 * @author  Andrew
 * @date  2020/5/21 9:44
 * 替换字体工具
 */
object TypefaceUtil {

    private fun replaceFont(@NotNull root: View, fontPath: String) {
        if (TextUtils.isEmpty(fontPath)) {
            return
        }
        if (root is TextView) {
            var style = Typeface.NORMAL
            if (root.typeface != null) style = root.typeface.style
            root.setTypeface(createTypeface(root.getContext(), fontPath), style)
        } else if (root is ViewGroup) {
            for (value in 0..root.childCount) {
                replaceFont(root.getChildAt(value), fontPath)
            }
        }
    }

    fun replaceFont(@NotNull activity: Activity, fontPath: String) {
        replaceFont(getRootView(activity),fontPath);
    }

    private fun createTypeface(context: Context, fontPath: String): Typeface {
        return Typeface.createFromAsset(context.assets, fontPath)
    }

    private fun getRootView(activity: Activity): View {
        return (activity.findViewById(android.R.id.content) as ViewGroup).getChildAt(0)
    }
}