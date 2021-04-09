package com.example.lostandfoundoncampus.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lostandfoundoncampus.R;

import androidx.fragment.app.Fragment;

public class BaiDuFragment extends Fragment {
    public static final String TAG = "content";
    private View view;
    private TextView textView;
    private String content;

    public static BaiDuFragment newInstance() {
        BaiDuFragment fragment = new BaiDuFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
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
        view = inflater.inflate(R.layout.fragment_baidu, container, false);
        init();
        return view;
    }

    private void init() {
        textView = (TextView) view.findViewById(R.id.tv_bai_du);
//        textView.setText(content);
    }
}