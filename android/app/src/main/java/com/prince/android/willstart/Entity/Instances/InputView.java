package com.prince.android.willstart.Entity.Instances;

import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Prince Bansal Local on 9/17/2016.
 */

public class InputView {

    private EditText inputField;
    private CheckBox inputCheck;

    public EditText getInputField() {
        return inputField;
    }

    public void setInputField(EditText inputField) {
        this.inputField = inputField;
    }

    public CheckBox getInputCheck() {
        return inputCheck;
    }

    public void setInputCheck(CheckBox inputCheck) {
        this.inputCheck = inputCheck;
    }
}
