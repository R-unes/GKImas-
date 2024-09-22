package com.example.gkimas_test.ui.dashboard;


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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.KeyEvent;

import com.example.gkimas_test.R;

import com.example.gkimas_test.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
    private EditText voText, daText, viText,voper,daper,viper;
    private TextView total,voplus,daplus,viplus;
    private Button button, resetButton;
    private FragmentDashboardBinding binding;
    private RadioButton voMainRadioButton, daMainRadioButton, viMainRadioButton;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        voMainRadioButton = view.findViewById(R.id.vomain);
        daMainRadioButton = view.findViewById(R.id.damain);
        viMainRadioButton = view.findViewById(R.id.vimain);
        voplus=view.findViewById(R.id.voplus);
        daplus=view.findViewById(R.id.daplus);
        viplus=view.findViewById(R.id.viplus);
        voText = view.findViewById(R.id.vo);
        daText = view.findViewById(R.id.da);
        viText = view.findViewById(R.id.vi);
        voper = view.findViewById(R.id.vo_per);
        daper = view.findViewById(R.id.da_per);
        viper = view.findViewById(R.id.vi_per);
        total = view.findViewById(R.id.totalproperty);
        button = view.findViewById(R.id.putout);
        resetButton = view.findViewById(R.id.reset);
        addTextWatcher(voText);
        addTextWatcher(daText);
        addTextWatcher(viText);
        addTextWatcher(voper);
        addTextWatcher(daper);
        addTextWatcher(viper);
        setEditorActionListener(voText);
        setEditorActionListener(daText);
        setEditorActionListener(viText);
        setEditorActionListener(voper);
        setEditorActionListener(daper);
        setEditorActionListener(viper);
        voMainRadioButton.setOnClickListener(v -> {
            if (voMainRadioButton.isChecked()) {
                daMainRadioButton.setChecked(false);
                viMainRadioButton.setChecked(false);
            }
        });

        daMainRadioButton.setOnClickListener(v -> {
            if (daMainRadioButton.isChecked()) {
                voMainRadioButton.setChecked(false);
                viMainRadioButton.setChecked(false);
            }
        });

        viMainRadioButton.setOnClickListener(v -> {
            if (viMainRadioButton.isChecked()) {
                voMainRadioButton.setChecked(false);
                daMainRadioButton.setChecked(false);
            }
        });

        button.setOnClickListener(v -> {
            String voInput = voText.getText().toString();
            String daInput = daText.getText().toString();
            String viInput = viText.getText().toString();

            if (TextUtils.isEmpty(voInput) || TextUtils.isEmpty(daInput) || TextUtils.isEmpty(viInput)) {
                showAlertDialog("没有参数", "请输入三维");
                return;
            }



            // 计算voplus, daplus, viplus
            int vo = Integer.parseInt(voInput);
            int da = Integer.parseInt(daInput);
            int vi = Integer.parseInt(viInput);
            double voPer = TextUtils.isEmpty(voper.getText()) ? 0 : Double.parseDouble(voper.getText().toString());
            double daPer = TextUtils.isEmpty(daper.getText()) ? 0 : Double.parseDouble(daper.getText().toString());
            double viPer = TextUtils.isEmpty(viper.getText()) ? 0 : Double.parseDouble(viper.getText().toString());

            if (voMainRadioButton.isChecked()) {
                int voResultValue = (int) Math.floor(vo + (1+voPer / 100) * 310);
                int daResultValue = (int) Math.floor(da + (1+daPer / 100) * 145);
                int viResultValue = (int) Math.floor(vi + (1+viPer / 100) * 145);

                voplus.setText(String.valueOf(voResultValue));
                daplus.setText(String.valueOf(daResultValue));
                viplus.setText(String.valueOf(viResultValue));
                total.setText(String.valueOf(voResultValue+daResultValue+viResultValue+90));
            } else if (daMainRadioButton.isChecked()) {
                int voResultValue = (int) Math.floor(vo + (1+voPer / 100) * 145);
                int daResultValue = (int) Math.floor(da + (1+daPer / 100) * 310);
                int viResultValue = (int) Math.floor(vi + (1+viPer / 100) * 145);

                voplus.setText(String.valueOf(voResultValue));
                daplus.setText(String.valueOf(daResultValue));
                viplus.setText(String.valueOf(viResultValue));
                total.setText(String.valueOf(voResultValue+daResultValue+viResultValue+90));
            } else if (viMainRadioButton.isChecked()) {
                int voResultValue = (int) Math.floor(vo + (1+voPer / 100) * 145);
                int daResultValue = (int) Math.floor(da + (1+daPer / 100) * 145);
                int viResultValue = (int) Math.floor(vi + (1+viPer / 100) * 310);

                voplus.setText(String.valueOf(voResultValue));
                daplus.setText(String.valueOf(daResultValue));
                viplus.setText(String.valueOf(viResultValue));
                total.setText(String.valueOf(voResultValue+daResultValue+viResultValue+90));
            }
        });


        resetButton.setOnClickListener(v -> {
            voText.setText("");
            daText.setText("");
            viText.setText("");
            voper.setText("");
            daper.setText("");
            viper.setText("");
            voplus.setText("");
            daplus.setText("");
            viplus.setText("");
            total.setText("");
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

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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


    private boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false; // 空字符串不是数字
        }
        return str.matches("-?\\d+(\\.\\d+)?"); // 支持负数和小数
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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