package com.example.barberiashop_app.ui.turnos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.databinding.FragmentTurnosBinding;

public class TurnosFragment extends Fragment {

//    private TurnosViewModel mViewModel;
//
//    public static TurnosFragment newInstance() {
//        return new TurnosFragment();
//    }
//

    private FragmentTurnosBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TurnosViewModel turnosViewModel =
            new ViewModelProvider(this).get(TurnosViewModel.class);

        binding = FragmentTurnosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTurnos;
        turnosViewModel
                .getText()
                .observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(TurnosViewModel.class);
//        // TODO: Use the ViewModel
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}