package com.example.lostandfoundoncampus.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lostandfoundoncampus.R;

import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {
    public static final String TAG = "content";
    private View view;
    private TextView textView;
    private String content;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
//        Bundle args = new Bundle();
////        args.putString(TAG, content);
////        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
//        content = bundle != null ? bundle.getString(TAG) : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        init();
        return view;
    }

    private void init() {
        textView = (TextView) view.findViewById(R.id.tv_setting);
//        textView.setText(content);
    }
}
