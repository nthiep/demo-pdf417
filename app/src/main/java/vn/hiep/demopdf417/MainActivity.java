package vn.hiep.demopdf417;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.MenuItem;

import vn.hiep.demopdf417.fragments.OnFragmentInteractionListener;
import vn.hiep.demopdf417.fragments.ResultFragment;
import vn.hiep.demopdf417.fragments.ScanFragment;
import vn.hiep.demopdf417.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    final ScanFragment scanFragment = ScanFragment.newInstance();
    final ResultFragment resultFragment = ResultFragment.newInstance();
    final SettingFragment settingFragment = SettingFragment.newInstance();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = scanFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = (@NonNull MenuItem item) -> {
        switch (item.getItemId()) {
            case R.id.navigation_scan:
                fm.beginTransaction().hide(active).show(scanFragment).commit();
                active = scanFragment;
                scanFragment.startScan();
                return true;
            case R.id.navigation_results:
                fm.beginTransaction().hide(active).show(resultFragment).commit();
                active = resultFragment;
                scanFragment.stopScan();
                return true;
            case R.id.navigation_settings   :
                fm.beginTransaction().hide(active).show(settingFragment).commit();
                active = settingFragment;
                scanFragment.stopScan();
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        fm.beginTransaction().add(R.id.main_container, settingFragment, "3").hide(settingFragment).commit();
        fm.beginTransaction().add(R.id.main_container, resultFragment, "2").hide(resultFragment).commit();
        fm.beginTransaction().add(R.id.main_container,scanFragment, "1").commit();
    }

    @Override
    public void onFragmentInteraction(String result) {
        resultFragment.addResult(result);
    }
}
