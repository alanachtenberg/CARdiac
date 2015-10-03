package com.appspot.cadiac_404.androidapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appspot.cadiac_404.androidapp.popup.PopUpDialogFragment;

/**
 * Created by Alan on 9/21/2015.
 */
public class DeveloperFragment  extends Fragment{
    private View mView;
    private Button mPopUpButton;
    private Button button2;
    private Button button3;
    public DeveloperFragment(){}
    public static DeveloperFragment newInstance() {
        DeveloperFragment fragment = new DeveloperFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_developer, container,false);
        mPopUpButton = (Button)mView.findViewById(R.id.pop_up_test_button);
        setUpButtons();
        return mView;
    }

    private void setUpButtons(){
        mPopUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt(PopUpDialogFragment.TIMEOUT_ARG_KEY,10);
                PopUpDialogFragment popUp = new PopUpDialogFragment();
                popUp.setArguments(args);
                popUp.show(getFragmentManager(), "pop_up_dialog_fragment");
            }
        });
    }
}
