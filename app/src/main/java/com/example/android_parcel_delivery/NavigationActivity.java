package com.example.android_parcel_delivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_parcel_delivery.libUtils.MenuItemData;
import com.example.android_parcel_delivery.libUtils.OkHttpUtils;
import com.example.android_parcel_delivery.libUtils.Utils;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    List<MenuItemData> menuItems = new ArrayList<>();
    int lastGeneratedId = 0;
    TextView headerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        //headerName = findViewById(R.id.headerName);

        //step 1 - set up toolbar
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar ,R.string.OpenDrawer,R.string.CloseDrawer);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout ,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        //toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fetchMenuData();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selectedItemId = item.getItemId();
                Log.d("TAG","id:" +selectedItemId);
                if (selectedItemId== 1000) { //for logout
                    performLogout();
                }else if (isDynamicMenuItem(selectedItemId)){
                    handleDynamicMenuItemClick(selectedItemId);
                }else{
                    Log.d("TAG","static");
                    handleStaticMenuItemClick(selectedItemId);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void handleStaticMenuItemClick(int itemId) {

    }

    private void handleDynamicMenuItemClick(int itemId) {
        for (MenuItemData menuItem : menuItems) {
            if(menuItem.getResourceId() == itemId){
                String className = menuItem.getClassName();
                loadDynamicFragment(className);
                break;
            }
        }
    }

    private void performLogout() {
        Toast.makeText(NavigationActivity.this,"Logout",Toast.LENGTH_SHORT).show();
        String url = Utils.makeURL("api/logout.php");
        Map<String, String> authData = Utils.getAuth(this);
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        okHttpUtils.postRequest(url, authData, new OkHttpUtils.ResponseCallback() {
            @Override
            public void onResponse(JSONObject resp) {
                try{
                    int err = resp.getInt("err");
                    if(err == 0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lastGeneratedId = 0;
                                Intent intent = new Intent(NavigationActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private boolean isDynamicMenuItem(int itemId) {
        for (MenuItemData menuItem : menuItems) {
            if (menuItem.getResourceId() == itemId){
                return true;
            }
        }
        return false;
    }

    private void fetchMenuData() {
        String url = Utils.makeURL("api/get_menu.php");
        Map<String, String> authData = Utils.getAuth(this);
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        okHttpUtils.postRequest(url, authData, new OkHttpUtils.ResponseCallback() {
            @Override
            public void onResponse(JSONObject resp) {
                Log.d("TAG","menuRe - " + resp.toString());
                try{
                    int err = resp.getInt("err");
                    String msg = resp.getString("msg");
                    String user_id = resp.getString("user_id");


                    if(err == 0){
                        JSONArray data = resp.getJSONArray("data");
                        Log.d("TAG","msg-" + data);
                        //headerName.setText(user_id);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateNavigationView(data);
                                updateHeaderTitle(user_id);
                            }
                        });


                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }


    private void updateHeaderTitle(String title) {
        View headerView = navigationView.getHeaderView(0);
        headerName = headerView.findViewById(R.id.headerName);
        headerName.setText(title);
    }



    private void populateNavigationView(JSONArray data) {
        Menu menu = navigationView.getMenu();
        menuItems.clear();
        for(int i=0; i< data.length(); i++){
            try {
                JSONObject menuItem = data.getJSONObject(i);
                String id = menuItem.getString("btn_id");
                String title = menuItem.getString("title");
                String icon = menuItem.getString("icon");
                String className = menuItem.getString("fragmentClassName");
                int sl = menuItem.getInt("sl");

                //int iconResId = getResources().getIdentifier(icon, "drawable", getPackageName());
                menuItems.add(new MenuItemData(sl,id, title, icon, className));


                // Add menu item to NavigationView
                //int itemId = View.generateViewId(); // Generate unique id for each item
                int itemId = lastGeneratedId + i +1;
                MenuItem item = menu.add(Menu.NONE, itemId, Menu.NONE, title);
                //item.setIcon(iconResId);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        // Add Logout menu item
        //menu.add(Menu.NONE, R.id.optLogout, Menu.NONE, "Logout");
        MenuItem logoutItem = menu.add(Menu.NONE, 1000, Menu.NONE, "Logout");
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){ // use android.R.id
            drawerLayout.openDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }
     */

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    private void loadDynamicFragment(String className) {
        try {
            Class<?> fragmentClass = Class.forName(className);
            Fragment fragment = (Fragment) fragmentClass.getConstructor().newInstance();
            loadFragment(fragment);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void loadMenu(Activity activity) {
        Intent intent = new Intent(NavigationActivity.this,activity.getClass());
        startActivity(intent);
    }
}