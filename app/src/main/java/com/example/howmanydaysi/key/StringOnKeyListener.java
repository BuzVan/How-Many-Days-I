package com.example.howmanydaysi.key;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class StringOnKeyListener implements View.OnKeyListener{
    private EditText editText;
    private Context context;

    public StringOnKeyListener(EditText editText, Context context) {
        this.editText =editText;
        this.context=context;

    }
    public boolean onKey(View v, int keyCode, KeyEvent event) {
//обработка нажатия на enter при вводе  события
        boolean consumed = false;
        if (keyCode == KEYCODE_ENTER) {
                editText.setCursorVisible(false);
                //скрываем клавиатуру
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                consumed = true;
        }
        return consumed;
    }
}
