package ru.elron.weather.extensions

import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun EditText.hideKeyboardAndClearFocus() {
    val imm: InputMethodManager? =
        ContextCompat.getSystemService<InputMethodManager?>(
            context,
            InputMethodManager::class.java
        )
    imm?.hideSoftInputFromWindow(
        this.windowToken,
        0
    )
    this.clearFocus()
}

val KeyEvent.isEnter: Boolean
    get() = action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER

fun Fragment.showToast(stringRes: Int) {
    Toast.makeText(requireContext(), stringRes, Toast.LENGTH_SHORT).show()
}
