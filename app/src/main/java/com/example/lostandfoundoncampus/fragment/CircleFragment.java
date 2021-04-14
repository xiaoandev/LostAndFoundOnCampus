package com.example.lostandfoundoncampus.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lostandfoundoncampus.R;
import com.example.lostandfoundoncampus.bean.Publish;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.fragment.app.Fragment;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CircleFragment extends Fragment {
    public static final String TAG = "content";
    private View view;
    private TextView textView;
    private String content;

    public static CircleFragment newInstance() {
        CircleFragment fragment = new CircleFragment();
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
        view = inflater.inflate(R.layout.fragment_circle, container, false);
        init();
        return view;
    }

    private void init() {
        textView = (TextView) view.findViewById(R.id.tv_circle);
//        textView.setText(content);
    }

    private void loadData() {
        BmobQuery<Publish> bmobQuery = new BmobQuery<Publish>();
        bmobQuery.findObjects(new FindListener<Publish>() {
            @Override
            public void done(List<Publish> list, BmobException e) {
                if (e == null) {
                    //数据倒序显示，最新的数据在最上面
                    Collections.reverse(list);

                }
            }
        });
    }
}
