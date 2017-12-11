package com.jasperhale.myprivacy.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.jasperhale.myprivacy.Activity.Base.BaseActivity;
import com.jasperhale.myprivacy.Activity.Base.LogUtil;
import com.jasperhale.myprivacy.Activity.View.AppListFragment;
import com.jasperhale.myprivacy.Activity.View.SettingActivity;
import com.jasperhale.myprivacy.Activity.ViewModel.ViewModel;
import com.jasperhale.myprivacy.Activity.ViewModel.mViewModel;
import com.jasperhale.myprivacy.Activity.presenter.Presenter;
import com.jasperhale.myprivacy.Activity.presenter.mPresenter;
import com.jasperhale.myprivacy.R;
import com.jasperhale.myprivacy.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener, ViewPager.OnPageChangeListener {

    private ActivityMainBinding binding;
    private MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private Toolbar toolbar;
    private SearchView searchView;

    public Presenter presenter;

    private List<AppListFragment> appListFragment = new ArrayList<>(3);
    private List<ViewModel> viewModels = new ArrayList<>(3);
    private ViewModel User_viewModel;
    private ViewModel System_viewModel;
    private ViewModel Limted_viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化 binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //设置toolbar
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        //ViewPage适配器
        mSectionsPagerAdapter = new MainActivity.SectionsPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(mSectionsPagerAdapter);
        binding.viewPager.addOnPageChangeListener(this);
        binding.viewPager.setOffscreenPageLimit(2);

        binding.tabLayout.setupWithViewPager(binding.viewPager);

        //获取Presenter实例
        presenter = new mPresenter();

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        //释放viemmodel
        viewModels.clear();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
                presenter.Refresh_User();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextChange(String query) {
        /*
        LogUtil.d("Search",query);
        presenter.SeaechView(query, binding.tabLayout.getSelectedTabPosition());
        */
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int position,
                               float positionOffset,
                               int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (!searchView.getQuery().equals("")){
            //presenter.SeaechView(searchView.getQuery().toString(),position);
        }
    }

    //
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建Fragment mViewModel 实例
            AppListFragment fragment = new AppListFragment();
            fragment.setPosition(position);
            appListFragment.add(position, fragment);

            viewModels.add(position, new mViewModel());
            viewModels.get(position).setAdapter(appListFragment.get(position).getAdapter());

            presenter.addViewModel(viewModels.get(position), position);
            //fragment.setPresenter(presenter);

            return appListFragment.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "用户应用";
                case 1:
                    return "系统应用";
                case 2:
                    return "已限制应用";
                default:
                    return "";
            }
        }
    }

}


