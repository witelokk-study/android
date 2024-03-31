package com.witelokk.prac7;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.witelokk.prac7.databinding.DialogCustomBinding;
import com.witelokk.prac7.databinding.FragmentDialogsBinding;

import java.util.Calendar;

public class DialogsFragment extends Fragment {
    FragmentDialogsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogsBinding.inflate(inflater, container, false);

        binding.buttonAlertDialog.setOnClickListener(v -> {
//            // old one
//            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            // new one
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());

            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to perform this action?");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);

            builder.setPositiveButton("Yes", (dialog, which) -> {
                binding.alertDialogResult.setText("AlertDialog: Yes");
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Обработка отмены действия
                binding.alertDialogResult.setText("AlertDialog: Cancel");
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        binding.buttonPickDate.setOnClickListener(v -> {
//            // old one
//            DatePickerDialog datePickerDialog = new DatePickerDialog(
//                    requireActivity(),
//                    (view, year, month, dayOfMonth) -> {
//                        binding.date.setText(String.format("%02d-%02d-%04d",dayOfMonth, month, year));
//                    },
//                    2024,
//                    3,
//                    28);
//            datePickerDialog.show();

            // new one
            MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                    .build();
            materialDatePicker.addOnPositiveButtonClickListener(time -> {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(time);
                binding.date.setText(DateFormat.format("dd.MM.yyyy", cal).toString());
            });
            materialDatePicker.show(requireActivity().getSupportFragmentManager(), null);
        });

        binding.buttonPickTime.setOnClickListener(v -> {
//            // old one
//            TimePickerDialog timePickerDialog = new TimePickerDialog(
//                    requireActivity(),
//                    (view, hour, minute) -> {
//                        binding.time.setText(String.format("%02d:%02d", hour, minute));
//                    },
//                    20,
//                    4,
//                    true);
//            timePickerDialog.show();

            // new one
            MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                    .setHour(8)
                    .setMinute(4)
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .build();
            materialTimePicker.addOnPositiveButtonClickListener(tpv -> {
                binding.time.setText(String.format("%02d:%02d", materialTimePicker.getHour(), materialTimePicker.getMinute()));
            });
            materialTimePicker.show(requireActivity().getSupportFragmentManager(), null);
        });

        binding.buttonCusomDialog.setOnClickListener(v -> {
            Dialog dialog = new Dialog(requireActivity());
            DialogCustomBinding dialogBinding = DialogCustomBinding.inflate(getLayoutInflater());
            dialog.setContentView(dialogBinding.getRoot());
            dialog.show();
        });

        return binding.getRoot();
    }
}
