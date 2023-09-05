package com.nicare.ves.ui.auth.user;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.nicare.ves.R;
import com.nicare.ves.common.Util;
import com.nicare.ves.di.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import dmax.dialog.SpotsDialog;

public class FirstFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory mProviderFactory;
    FirstFragmentViewModel viewModel;
    private MaterialButton mViewById;
    private TextView txtEmail;
    private AlertDialog mAlertDialog;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAlertDialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setCancelable(false)
                .setMessage("Please wait, signing in..")
                .build();
        viewModel = ViewModelProviders.of(this, mProviderFactory).get(FirstFragmentViewModel.class);
        viewModel.observeSend().observe(getActivity(), baseResponseAuthResource -> {
            switch (baseResponseAuthResource.status) {
                case SUCCESS:
                    toSuccess();
                    break;

                default:
                    hideDialogue();
                    Util.showDialogueMessae(getActivity(), baseResponseAuthResource.data.getMessage(), "Message");

            }
        });

        mViewById = view.findViewById(R.id.btnLogin);
        txtEmail = view.findViewById(R.id.txtId);

        mViewById.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(txtEmail.getText().toString())) {
                  hideDialogue();
                Util.showDialogueMessae(getActivity(), "Incorrect login credential please provide valid  I.D", "Invalid Login");
                txtEmail.requestFocus();
            } else {
                viewModel.sendMail(txtEmail.getText().toString());
            }
        });
    }

    private void toSuccess() {
        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }
    private void hideDialogue() {
        mAlertDialog.dismiss();
//        mViewById.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideDialogue();

    }
}