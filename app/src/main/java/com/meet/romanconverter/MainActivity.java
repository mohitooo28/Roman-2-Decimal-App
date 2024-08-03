package com.meet.romanconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean isRomanUpdating = false;
    private boolean isDecimalUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        EditText decimal = findViewById(R.id.editTextTextPersonName2);
        EditText roman = findViewById(R.id.editTextTextPersonName);

        roman.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isDecimalUpdating) return;

                isRomanUpdating = true;
                String romanText = s.toString();
                if (romanText.isEmpty()) {
                    decimal.setText("");
                } else {
                    try {
                        int decimalValue = romanToDecimal(romanText);
                        decimal.setText(String.valueOf(decimalValue));
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(MainActivity.this, "Invalid Roman numeral", Toast.LENGTH_SHORT).show();
                    }
                }
                isRomanUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

        decimal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isRomanUpdating) return;

                isDecimalUpdating = true;
                String decimalText = s.toString();
                if (decimalText.isEmpty()) {
                    roman.setText("");
                } else {
                    try {
                        int decimalValue = Integer.parseInt(decimalText);
                        String romanValue = decimalToRoman(decimalValue);
                        roman.setText(romanValue);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Invalid Decimal number", Toast.LENGTH_SHORT).show();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                isDecimalUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    hideKeyboard(currentFocus);
                }
            }
            v.performClick();
            return false;
        });

    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    int value(char r) {
        switch (r) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default: return -1;
        }
    }

    int romanToDecimal(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Input string is empty");
        }

        int res = 0;
        int prevValue = 0;

        for (int i = str.length() - 1; i >= 0; i--) {
            int value = value(str.charAt(i));

            if (value == -1) {
                throw new IllegalArgumentException("Invalid Roman numeral character");
            }

            if (value < prevValue) {
                res -= value;
            } else {
                res += value;
            }
            prevValue = value;
        }

        return res;
    }

    String decimalToRoman(int num) {
        if (num <= 0 || num > 3999) {
            throw new IllegalArgumentException("Number out of range (1-3999)");
        }

        String[] m = {"", "M", "MM", "MMM"};
        String[] c = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] x = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] i = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        return m[num / 1000] +
                c[(num % 1000) / 100] +
                x[(num % 100) / 10] +
                i[num % 10];
    }
}
