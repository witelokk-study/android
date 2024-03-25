package com.witelokk.prac6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.witelokk.prac6.databinding.FragmentBinding;

public class FirstFragment extends Fragment {
    FragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBinding.inflate(inflater, container, false);

        binding.textView.setText("1");

        return binding.getRoot();
    }
}
