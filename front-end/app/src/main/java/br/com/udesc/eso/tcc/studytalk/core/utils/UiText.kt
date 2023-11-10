package br.com.udesc.eso.tcc.studytalk.core.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> {
                val args = args.map {
                    if (it is StringResource) {
                        it.asString()
                    } else {
                        it
                    }
                }.toTypedArray()
                stringResource(resId, *args)
            }
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> {
                val args = args.map {
                    if (it is StringResource) {
                        it.asString(context)
                    } else {
                        it
                    }
                }.toTypedArray()
                context.getString(resId, *args)
            }
        }
    }
}