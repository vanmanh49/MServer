package com.vm.mserver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Path;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.PathInterpolator;

import com.vm.mserver.dao.CRUD;
import com.vm.mserver.fragment.FirstFragment;
import com.vm.mserver.fragment.SecondFragment;
import com.vm.mserver.fragment.ViewPagerAdapter;
import com.vm.mserver.util.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.content)
    View v;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private boolean isActive;
    private CRUD crud;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        crud = CRUD.getInstants(this);
        isActive = Config.isServerRun(this);

        initFAB();
        initContentViews();
    }

    private void initContentViews() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        firstFragment = FirstFragment.getInstance();
        secondFragment = SecondFragment.getInstance();
        adapter.addFragment(firstFragment, "Người đăng ký");
        adapter.addFragment(secondFragment, "Dữ liệu điểm");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                showPopUp();
                return true;
            case R.id.item_about:
                startActivity(new Intent(this, AboutActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            case R.id.item_exit:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.exit)
                        .setMessage("Bạn có muốn thoát không?")
                        .setNegativeButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopUp() {
        View v = findViewById(R.id.item_add);
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_add, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_add_contact:
                        firstFragment.showInputDialog(MainActivity.this);
                        return true;
                    case R.id.item_add_student:
                        secondFragment.showInputDialog(MainActivity.this);
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void initFAB() {
        fab.setBackgroundTintList(ColorStateList.valueOf(isActive ? getResources().getColor(R.color.colorAccent) :
                getResources().getColor(R.color.fab_none_press_color)));
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.lineTo(0.5f, 1.3f);
        path.lineTo(0.75f, 0.8f);
        path.lineTo(1.0f, 1.0f);
        PathInterpolator pathInterpolator = new PathInterpolator(path);

        final float from = 1.0f;
        final float to = 1.3f;

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(fab, View.SCALE_X, from, to);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(fab, View.SCALE_Y, from, to);
        ObjectAnimator translationZ = ObjectAnimator.ofFloat(fab, View.TRANSLATION_Z, from, to);

        AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(scaleX, scaleY, translationZ);
        set1.setDuration(100);
        set1.setInterpolator(new AccelerateInterpolator());

        set1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fab.setImageResource(isActive ? R.drawable.ic_mail_outline_24dp : R.drawable.ic_mail_24dp);
                fab.setBackgroundTintList(ColorStateList.valueOf(isActive ? getResources().getColor(R.color.colorAccent) :
                        getResources().getColor(R.color.fab_none_press_color)));
            }
        });

        ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(fab, View.SCALE_X, to, from);
        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(fab, View.SCALE_Y, to, from);
        ObjectAnimator translationZBack = ObjectAnimator.ofFloat(fab, View.TRANSLATION_Z, to, from);

        AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(scaleXBack, scaleYBack, translationZBack);
        set2.setDuration(300);
        set2.setInterpolator(pathInterpolator);

        final AnimatorSet set = new AnimatorSet();
        set.playSequentially(set1, set2);

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fab.setClickable(true);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                fab.setClickable(false);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set.start();
                isActive = !isActive;
                Config.setServerStatus(getApplicationContext(), isActive);
                Snackbar.make(view, isActive ? "Đã bật Server!" : "Đã tắt Server!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
