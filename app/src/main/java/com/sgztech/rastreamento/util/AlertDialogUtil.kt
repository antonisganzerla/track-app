package com.sgztech.rastreamento.util

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sgztech.rastreamento.R

object AlertDialogUtil {

    /**
     * builder with only positive callback
     */
    fun create(
        context: Context,
        resourceMessage: Int,
        positiveCallBack : () -> Unit
    ): AlertDialog{
        return build(context, resourceMessage, positiveCallBack)
            .setNegativeButton(context.getString(R.string.dialog_negative_button)) { _, _ ->
                // unnecessary negative callback
            }
            .create()
    }

    private fun build(
        context: Context,
        resourceMessage: Int,
        positiveCallBack : () -> Unit
    ): AlertDialog.Builder{
        return AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.dialog_title))
            .setMessage(context.getString(resourceMessage))
            .setPositiveButton(context.getString(R.string.dialog_positive_button)) { _, _ ->
                positiveCallBack()
            }
    }

    fun buildCustomDialog(context: Context, resourceTitle: Int, view: View): AlertDialog.Builder{
        return buildSimpleDialog(context, resourceTitle)
            .setView(view)
    }

    fun buildSimpleDialog(context: Context, resourceTitle: Int): AlertDialog.Builder{
        return AlertDialog.Builder(context)
            .setTitle(context.getString(resourceTitle))
    }

    fun showSimpleDialog(context: Context, resourceTitle: Int, resourceMessage: Int){
        AlertDialog.Builder(context)
            .setTitle(context.getString(resourceTitle))
            .setMessage(context.getString(resourceMessage))
            .show()
    }
}