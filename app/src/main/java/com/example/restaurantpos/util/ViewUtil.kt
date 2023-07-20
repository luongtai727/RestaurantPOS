package com.example.restaurantpos.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast


/** 1. Change Activity by Intent */
fun Context.openActivity(pClass: Class<out Activity>) {
    startActivity(Intent(this, pClass))
}

fun Context.openActivity(pClass: Class<out Activity>, isFinish: Boolean = false) {
    startActivity(Intent(this, pClass))
    if (isFinish) {
        (this as Activity).finish()
    }
}


/** 2. Change Activity by Intent With Bundle*/
fun Context.openActivity(pClass: Class<out Activity>, bundle: Bundle?) {
    val intent = Intent(this, pClass)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(Intent(intent))
}

fun Context.openActivity(pClass: Class<out Activity>, isFinish: Boolean = false, bundle: Bundle?) {
    openActivity(pClass, bundle)
    if (isFinish) {
        (this as Activity).finish()
    }
}

/** 3. Hide, show, gone View */
fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}




fun Context.showToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

/**------------------------------------------------------------------------------------*/
fun View.disableView() {
    this.isClickable = false
    this.postDelayed({ this.isClickable = true }, 200)
}
class SafeClickListener(val onSafeClickListener: (View) -> Unit) : View.OnClickListener {
    override fun onClick(v: View) {
        v.disableView()
        onSafeClickListener(v)
    }
}
fun View.setOnSafeClick(onSafeClickListener: (View) -> Unit) {
    val safeClick = SafeClickListener {
        onSafeClickListener(it)
    }
    setOnClickListener(safeClick)
}