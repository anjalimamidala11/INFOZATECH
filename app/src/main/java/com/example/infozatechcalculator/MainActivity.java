
package com.example.infozatechcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private TextView tvFormula;

    private double firstNumber = 0;
    private String operator = "";
    private boolean newNumber = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        tvFormula = findViewById(R.id.tvFormula);

        tvDisplay.setText("0");

        connectNumber(R.id.btn0, "0");
        connectNumber(R.id.btn1, "1");
        connectNumber(R.id.btn2, "2");
        connectNumber(R.id.btn3, "3");
        connectNumber(R.id.btn4, "4");
        connectNumber(R.id.btn5, "5");
        connectNumber(R.id.btn6, "6");
        connectNumber(R.id.btn7, "7");
        connectNumber(R.id.btn8, "8");
        connectNumber(R.id.btn9, "9");

        findViewById(R.id.btnDecimal).setOnClickListener(v -> {
            String current = tvDisplay.getText().toString();

            if (newNumber) {
                tvDisplay.setText("0.");
                newNumber = false;
            } else if (!current.contains(".")) {
                tvDisplay.setText(current + ".");
            }
        });

        findViewById(R.id.btnAdd).setOnClickListener(v -> setOperator("+"));
        findViewById(R.id.btnSubtract).setOnClickListener(v -> setOperator("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> setOperator("*"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> setOperator("/"));

        findViewById(R.id.btnPercent).setOnClickListener(v -> {
            double value = getDisplayValue();
            tvDisplay.setText(formatNumber(value / 100));
            newNumber = true;
        });

        findViewById(R.id.btnSign).setOnClickListener(v -> {
            double value = -getDisplayValue();
            tvDisplay.setText(formatNumber(value));
        });

        findViewById(R.id.btnEquals).setOnClickListener(v -> calculate());

        findViewById(R.id.btnClear).setOnClickListener(v -> {
            tvDisplay.setText("0");
            tvFormula.setText("");
            firstNumber = 0;
            operator = "";
            newNumber = true;
        });

        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            if (newNumber) return;

            String current = tvDisplay.getText().toString();

            if (current.length() > 1) {
                current = current.substring(0, current.length() - 1);

                if (current.equals("-")) {
                    current = "0";
                }

                tvDisplay.setText(current);
            } else {
                tvDisplay.setText("0");
            }
        });
    }

    private void connectNumber(int buttonId, String digit) {
        Button button = findViewById(buttonId);

        button.setOnClickListener(v -> {
            String current = tvDisplay.getText().toString();

            if (newNumber || current.equals("0")) {
                tvDisplay.setText(digit);
                newNumber = false;
            } else {
                tvDisplay.setText(current + digit);
            }
        });
    }

    private double getDisplayValue() {
        try {
            return Double.parseDouble(
                    tvDisplay.getText().toString()
            );
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void setOperator(String selectedOperator) {
        firstNumber = getDisplayValue();
        operator = selectedOperator;

        String symbol = selectedOperator;
        if (selectedOperator.equals("*")) symbol = "×";
        if (selectedOperator.equals("/")) symbol = "÷";

        tvFormula.setText(
                formatNumber(firstNumber) + " " + symbol
        );

        newNumber = true;
    }


    private void calculate() {
        if (operator.isEmpty()) return;

        double secondNumber = getDisplayValue();

        if (operator.equals("/") && secondNumber == 0) {
            tvDisplay.setText("Error");
            tvFormula.setText("Cannot divide by zero");
            firstNumber = 0;
            operator = "";
            newNumber = true;
            return;
        }

        double result;

        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                result = firstNumber / secondNumber;
                break;
            default:
                return;
        }

        String symbol = operator.equals("*") ? "×"
                : operator.equals("/") ? "÷" : operator;

        tvFormula.setText(
                formatNumber(firstNumber) + " " + symbol + " "
                        + formatNumber(secondNumber) + " ="
        );

        tvDisplay.setText(formatNumber(result));

        firstNumber = result;
        operator = "";
        newNumber = true;
    }

    private String formatNumber(double number) {
        if (Double.isNaN(number) || Double.isInfinite(number)) {
            return "Error";
        }

        if (number == Math.floor(number)
                && Math.abs(number) < 1e15) {
            return String.valueOf((long) number);
        }

        return String.valueOf(number);
    }
}
