package com.example.gkimas_test.ui.home;

import static android.content.Context.INPUT_METHOD_SERVICE;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.KeyEvent;

import com.example.gkimas_test.R;

public class HomeFragment extends Fragment {
    private EditText voText, daText, viText;
    private TextView total, parameter;
    private Spinner grade;
    private Button button, resetButton;
    private String[] gradelist = {"A+", "A", "S", "S+", "SS", "SS+", "B+", "B"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        voText = view.findViewById(R.id.vo);
        daText = view.findViewById(R.id.da);
        viText = view.findViewById(R.id.vi);
        total = view.findViewById(R.id.totalproperty);
        grade = view.findViewById(R.id.grade);
        parameter = view.findViewById(R.id.parameter);
        button = view.findViewById(R.id.putout);
        resetButton = view.findViewById(R.id.reset);

        addTextWatcher(voText);
        addTextWatcher(daText);
        addTextWatcher(viText);
        setEditorActionListener(voText);
        setEditorActionListener(daText);
        setEditorActionListener(viText);

        button.setOnClickListener(v -> {
            String voInput = voText.getText().toString();
            String daInput = daText.getText().toString();
            String viInput = viText.getText().toString();

            if (TextUtils.isEmpty(voInput) || TextUtils.isEmpty(daInput) || TextUtils.isEmpty(viInput)) {
                showAlertDialog("没有参数", "请输入三维");
                return;
            }
            total.setText(String.valueOf(calculateSum()));
            parameter.setText(String.valueOf(calculatePoint()));
        });

        resetButton.setOnClickListener(v -> {
            voText.setText("");
            daText.setText("");
            viText.setText("");
            total.setText("");
            parameter.setText("");
            grade.setSelection(0);
        });

        return view;
    }

    private void addTextWatcher(final EditText textView) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    hideKeyboard(textView);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setEditorActionListener(final EditText textView) {
        textView.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE)) {
                hideKeyboard(textView);
                return true;
            }
            return false;
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private int calculatePoint() {
        String selectedGrade = grade.getSelectedItem().toString();
        int point = getPointForGrade(selectedGrade);
        int sum = calculateSum();
        return calculateXFromPoint(sum, point);
    }

    private int getPointForGrade(String grade) {
        switch (grade) {
            case "S":
                return 13000;
            case "A+":
                return 11500;
            case "A":
                return 10000;
            case "B+":
                return 8000;
            case "B":
                return 6000;
            case "S+":
                return 14500;
            case "SS":
                return 16000;
            case "SS+":
                return 17500;
            default:
                return 0;  // 默认值
        }
    }
    protected int calculateSum(){
        int vo = 0, da = 0, vi = 0;
        // 获取输入文本
        String voInput = voText.getText().toString();
        String daInput = daText.getText().toString();
        String viInput = viText.getText().toString();

        // 检查输入是否为空
        if (TextUtils.isEmpty(voInput) || TextUtils.isEmpty(daInput) || TextUtils.isEmpty(viInput)) {

            return 0; // 返回 0 或其他合适的默认值
        }

        // 检查输入是否为数字
        if (!isNumeric(voInput) || !isNumeric(daInput) || !isNumeric(viInput)) {
            showAlertDialog("输入错误", "请输入有效的数字");
            return 0; // 返回 0 或其他合适的默认值
        }

        // 转换输入为 int（向下取整）
        vo = (int) Math.floor(Double.parseDouble(voInput));
        da = (int) Math.floor(Double.parseDouble(daInput));
        vi = (int) Math.floor(Double.parseDouble(viInput));

        // 计算总和
        int sum = vo + da + vi + 90;
        return sum; // 返回总和
    }



    private int calculateXFromPoint(int sum, int point) {
        // 反推 para 值
        int calculatedPara = (int) Math.floor(point - 1700 - 2.3 * sum);
        // 检查 calculatedPara 是否在合法区间内
        if (calculatedPara < 0) {
            return -1; // 返回 -1 表示无效
        }
        // 反推 x 值
        if (calculatedPara > 0 && calculatedPara <= 1500) {
            return (int) Math.floor(calculatedPara / 0.3);
        } else if (calculatedPara > 1500 && calculatedPara <= 2250) {
            return (int) Math.floor((calculatedPara - 750) / 0.15);
        } else if (calculatedPara > 2250 && calculatedPara <= 3050) {
            return (int) Math.floor((calculatedPara - 1450) / 0.08);
        } else if (calculatedPara > 3050 && calculatedPara <= 3450) {
            return (int) Math.floor((calculatedPara - 2250) / 0.04);
        } else if (calculatedPara > 3450 && calculatedPara <= 3650) {
            return (int) Math.floor((calculatedPara - 2850) / 0.02);
        } else if (calculatedPara > 3650) {
            return (int) Math.floor((calculatedPara - 3250) / 0.01);
        } else {
            return -1; // 返回 -1 表示无效
        }
    }

    // 检查输入是否为数字的方法
    private boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false; // 空字符串不是数字
        }
        return str.matches("-?\\d+(\\.\\d+)?"); // 支持负数和小数
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }
}
