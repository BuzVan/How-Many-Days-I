package com.example.howmanydaysi.filters;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

public class FilterTime implements InputFilter {
    private EditText editText;
    public FilterTime(EditText editText) {
        this.editText =editText;
    }
//фильтр ввода времени
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.length() == 0) {
            return null;
        }
        String result = "";
        result += dest.toString().substring(0, dstart);
        result += source.toString().substring(start, end);
        result += dest.toString().substring(dend, dest.length());

        boolean allowEdit = true;
        char c;
        if (result.length() > 0) {
            c = result.charAt(0);
            allowEdit &= (c >= '0' && c <= '2');
        }
        if (result.length() > 1) {
            c = result.charAt(1);
            if(result.charAt(0) == '0' || result.charAt(0) == '1')
                allowEdit &= (c >= '0' && c <= '9');
            else
                allowEdit &= (c >= '0' && c <= '3');


        }
        if(result.length()==2 && allowEdit) {
            editText.setText(result + ":");
            editText.setSelection(editText.getText().length());
        }

        if (result.length() > 3) {
            c = result.charAt(3);
            allowEdit &= (c >= '0' && c <= '5');
        }
        if (result.length() > 4) {
            c = result.charAt(4);
            allowEdit &= (c >= '0' && c <= '9');
        }
        if (result.length() > 5) {
            allowEdit =false;
        }
        return allowEdit ? null : "";
    }
}
