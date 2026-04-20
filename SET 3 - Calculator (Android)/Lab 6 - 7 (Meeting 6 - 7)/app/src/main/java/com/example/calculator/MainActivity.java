package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private String currentInput = "";
    private Double previousResult = null;
    private String currentOperator = null;
    private boolean isNewInput = false;
    private boolean isError = false;
    private ArrayList<String> history = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);

        int[] digitIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, 
                          R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
                          
        View.OnClickListener digitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                appendDigit(b.getText().toString());
            }
        };
        for (int id : digitIds) {
            findViewById(id).setOnClickListener(digitListener);
        }

        View.OnClickListener opListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                handleOperator(b.getText().toString());
            }
        };
        findViewById(R.id.btnAdd).setOnClickListener(opListener);
        findViewById(R.id.btnSub).setOnClickListener(opListener);
        findViewById(R.id.btnMul).setOnClickListener(opListener);
        findViewById(R.id.btnDiv).setOnClickListener(opListener);
        findViewById(R.id.btnNum).setOnClickListener(opListener);

        findViewById(R.id.btnEquals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });

        findViewById(R.id.btnAC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });

        findViewById(R.id.btnCE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEntry();
            }
        });

        // Bases
        findViewById(R.id.btnBin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertBase(2);
            }
        });
        findViewById(R.id.btnOct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertBase(8);
            }
        });
        findViewById(R.id.btnHex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertBase(16);
            }
        });

        // History
        findViewById(R.id.btnHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                intent.putStringArrayListExtra("history", history);
                startActivity(intent);
            }
        });
    }

    private void convertBase(int radix) {
        if (isError) return;
        
        Double valueToConvert = null;
        if (!currentInput.isEmpty()) {
            valueToConvert = Double.parseDouble(currentInput);
        } else if (previousResult != null) {
            valueToConvert = previousResult;
        }

        if (valueToConvert == null) return; 

        if (valueToConvert == Math.floor(valueToConvert) && !Double.isInfinite(valueToConvert)) {
            long intVal = valueToConvert.longValue();
            String res = "";
            if (radix == 2) res = Long.toBinaryString(intVal);
            else if (radix == 8) res = Long.toOctalString(intVal);
            else if (radix == 16) res = Long.toHexString(intVal).toUpperCase();
            
            updateDisplay(res);
        } else {
            handleError();
        }
    }

    private void appendDigit(String digit) {
        if (isError) return;
        if (isNewInput) {
            currentInput = "";
            previousResult = null;
            currentOperator = null;
            isNewInput = false;
        }
        currentInput += digit;
        updateDisplay(currentInput);
    }

    private void handleOperator(String op) {
        if (isError) return;
        
        if (currentInput.isEmpty()) {
            if (previousResult != null) {
                currentOperator = op;
                isNewInput = false;
            }
            return;
        }

        if (previousResult == null) {
            previousResult = Double.parseDouble(currentInput);
        } else if (currentOperator != null) {
            String leftOp = formatResult(previousResult);
            Double intermediate = evaluate(previousResult, currentOperator, currentInput);
            if (isError) return;
            String rightOp = currentInput;
            
            String entry = leftOp + " " + currentOperator + " " + rightOp + " = " + formatResult(intermediate);
            history.add(entry);
            
            previousResult = intermediate;
            updateDisplay(formatResult(previousResult));
        }

        currentOperator = op;
        currentInput = "";
        isNewInput = false;
    }

    private void calculateResult() {
        if (isError || currentOperator == null || currentInput.isEmpty()) {
            return;
        }
        
        String leftOp = formatResult(previousResult);
        Double result = evaluate(previousResult, currentOperator, currentInput);
        if (isError) return;
        String rightOp = currentInput;
        
        String entry = leftOp + " " + currentOperator + " " + rightOp + " = " + formatResult(result);
        history.add(entry);
        
        previousResult = result;
        updateDisplay(formatResult(result));
        
        currentInput = "";
        currentOperator = null;
        isNewInput = true;
    }

    private Double evaluate(Double op1, String operator, String currentInputStr) {
        double op2 = Double.parseDouble(currentInputStr);
        double res = 0.0;
        switch (operator) {
            case "+": res = op1 + op2; break;
            case "-": res = op1 - op2; break;
            case "*": res = op1 * op2; break;
            case "/":
                if (op2 == 0) {
                    handleError();
                    return 0.0;
                }
                res = op1 / op2;
                break;
            case "num":
            case "^":
                res = Math.pow(op1, op2);
                break;
        }
        return res;
    }

    private void handleError() {
        isError = true;
        tvDisplay.setText("Error");
    }

    private void clearAll() {
        currentInput = "";
        previousResult = null;
        currentOperator = null;
        isNewInput = false;
        isError = false;
        updateDisplay("");
    }

    private void clearEntry() {
        if (isError) {
            clearAll();
            return;
        }
        currentInput = "";
        isNewInput = false;
        if (previousResult != null && !isError) {
            updateDisplay(formatResult(previousResult));
        } else {
            updateDisplay("");
        }
    }

    private void updateDisplay(String text) {
        tvDisplay.setText(text);
    }
    
    private String formatResult(Double value) {
        if (value == null) return "";
        if (value == value.longValue()) {
            return String.format("%d", value.longValue());
        } else {
            return String.valueOf(value);
        }
    }
}
