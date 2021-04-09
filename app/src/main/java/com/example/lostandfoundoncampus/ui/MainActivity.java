package com.example.lostandfoundoncampus.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.bmob.v3.BmobUser;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfoundoncampus.BaseActivity;
import com.example.lostandfoundoncampus.R;
import com.example.lostandfoundoncampus.fragment.BaiDuFragment;
import com.example.lostandfoundoncampus.fragment.HomeFragment;
import com.example.lostandfoundoncampus.fragment.SettingFragment;
import com.example.lostandfoundoncampus.fragment.TaoBaoFragment;
import com.example.lostandfoundoncampus.utils.CommonUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<Fragment> fragments;
    private HomeFragment homeFragment;
    private TaoBaoFragment taoBaoFragment;
    private BaiDuFragment baiDuFragment;
    private SettingFragment settingFragment;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AppManager.getInstance().addActivity(this);

        //初始标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        fragments = new ArrayList<Fragment>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(BaiDuFragment.newInstance());
        fragments.add(TaoBaoFragment.newInstance());
        fragments.add(SettingFragment.newInstance());

        //初始化主页内容
//        initFragment1();
        switchToFragment(0);

//        if (savedInstanceState == null)
//            selectItem(0);

        NavigationView navigationview = (NavigationView) findViewById(R.id.navigation_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0);
        drawer.setDrawerListener(toggle);//初始化状态
        toggle.syncState();


        /*---------------------------添加头布局和尾布局-----------------------------*/
        //获取xml头布局view
        final View headerView = navigationview.getHeaderView(0);
        //添加头布局的另外一种方式
        //View headview=navigationview.inflateHeaderView(R.layout.navigationview_header);

        //寻找头部里面的控件
        ImageView imageView = headerView.findViewById(R.id.iv_head);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了头像", Toast.LENGTH_SHORT).show();
            }
        });

        TextView currentUserName = headerView.findViewById(R.id.current_user);
        currentUserName.setText(BmobUser.getCurrentUser(BmobUser.class).getUsername());

        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
        ColorStateList csl = (ColorStateList) getResources().getColorStateList(R.color.nav_menu_text_color);
        //设置item的条目颜色
        navigationview.setItemTextColor(csl);
        //去掉默认颜色显示原来颜色  设置为null显示本来图片的颜色
        navigationview.setItemIconTintList(csl);

        //设置单个消息数量
//        LinearLayout llAndroid = (LinearLayout) navigationview.getMenu().findItem(R.id.single_1).getActionView();
//        TextView msg= (TextView) llAndroid.findViewById(R.id.msg_bg);
//        msg.setText("99+");

        //设置条目点击监听
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_android:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
//                        initFragment1();
                        switchToFragment(0);
                        //关闭侧边栏
                        drawer.closeDrawers();
                        break;
                    case R.id.item_bai_du:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
//                        initFragment2();
                        switchToFragment(1);
                        //关闭侧边栏
                        drawer.closeDrawers();
                        break;
                    case R.id.item_tao_bao:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
//                        initFragment3();
                        switchToFragment(2);
                        //关闭侧边栏
                        drawer.closeDrawers();
                        break;
                    default:
                        break;
                }


//                if (menuItem.getTitle().equals("安卓")) {
//                    selectItem(0);
//                } else if (menuItem.getTitle().equals("百度")) {
//                    selectItem(1);
//                } else if (menuItem.getTitle().equals("淘宝")) {
//                    selectItem(2);
//                } else {
//                    selectItem(3);
//                }
//                Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();

                //设置哪个按钮被选中
//                menuItem.setChecked(true);
                return false;
            }
        });
    }

    private void selectItem(int position) {
        Fragment fragment = fragments.get(position);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_fragment, fragment).commit();

    }

//    /**
//     * 切换fragment（方式一：hide + show）
//     * @param position
//     */
//    private void switchToFragment(int position) {
//        Fragment fragment = fragments.get(position);
//        //开启事务，fragment的控制是由事务来实现
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
//        transaction.add(R.id.content_fragment, fragment);
//        //隐藏所有fragment
//        hideFragment(transaction);
//        //显示需要显示的fragment
//        transaction.show(fragment);
//        //提交事务
//        transaction.commit();
//    }

    /**
     * 切换fragment（方式二：replace）
     * @param position
     */
    private void switchToFragment(int position) {
        Fragment fragment = fragments.get(position);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_fragment, fragment).commit();
    }

    //显示第一个Fragment
    private void initFragment1() {
        //开启事务，fragment的控制是由事务来实现
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            transaction.add(R.id.content_fragment, homeFragment);
        }

        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(homeFragment);

        //第二种方式（replace）, 初始化fragment
//        if (homeFragment == null) {
//            homeFragment = new HomeFragment();
//        }
//        transaction.replace(R.id.content_fragment, homeFragment);
        //提交事务
        transaction.commit();
    }

    //显示第二个fragment
    private void initFragment2() {
        //开启事务，fragment的控制是由事务来实现
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if (baiDuFragment == null) {
            baiDuFragment = new BaiDuFragment();
            transaction.add(R.id.content_fragment, baiDuFragment);
        }

        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(baiDuFragment);

        //第二种方式（replace）, 初始化fragment
//        if (baiDuFragment == null) {
//            baiDuFragment = new BaiDuFragment();
//        }
//        transaction.replace(R.id.content_fragment, baiDuFragment);
        //提交事务
        transaction.commit();
    }

    //显示第三个fragment
    private void initFragment3() {
        //开启事务，fragment的控制是由事务来实现
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if (taoBaoFragment == null) {
            taoBaoFragment = new TaoBaoFragment();
            transaction.add(R.id.content_fragment, taoBaoFragment);
        }

        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(taoBaoFragment);

        //第二种方式（replace）, 初始化fragment
//        if (taoBaoFragment == null) {
//            taoBaoFragment = new TaoBaoFragment();
//        }
//        transaction.replace(R.id.content_fragment, taoBaoFragment);
        //提交事务
        transaction.commit();
    }

    //显示第个fragment
    private void initFragment4() {
        //开启事务，fragment的控制是由事务来实现
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if (settingFragment == null) {
            settingFragment = new SettingFragment();
            transaction.add(R.id.content_fragment, settingFragment);
        }

        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(settingFragment);

        //第二种方式（replace）, 初始化fragment
//        if (settingFragment == null) {
//            settingFragment = new SettingFragment();
//        }
//        transaction.replace(R.id.content_fragment, settingFragment);
        //提交事务
        transaction.commit();
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null)
            transaction.hide(homeFragment);
        if (taoBaoFragment != null)
            transaction.hide(taoBaoFragment);
        if (baiDuFragment != null)
            transaction.hide(baiDuFragment);
        if (settingFragment != null)
            transaction.hide(settingFragment);
    }

    public void loginOut(View view) {
        CommonUtils.showLogoutDialog(MainActivity.this);
    }

    public void settingBtn(View view) {
        Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
        switchToFragment(3);
        //关闭侧边栏
        drawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        CommonUtils.showExitDialog(MainActivity.this);
    }
}