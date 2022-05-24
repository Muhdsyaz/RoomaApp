package my.edu.utem.ftmk.bitp3453.rooma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class AdsHistory extends AppCompatActivity {

    private TabLayout tlAdsTab;
    private ViewPager2 viewPager2;
    private AdsFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_history);

        tlAdsTab = findViewById(R.id.tlAdsTab);
        viewPager2 = findViewById(R.id.viewPager2);

        tlAdsTab.addTab(tlAdsTab.newTab().setText("Live Ads"));
        tlAdsTab.addTab(tlAdsTab.newTab().setText("Sold Ads"));
        tlAdsTab.addTab(tlAdsTab.newTab().setText("Bumped Ads"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new AdsFragmentAdapter(fragmentManager , getLifecycle());
        viewPager2.setAdapter(adapter);

        tlAdsTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tlAdsTab.selectTab(tlAdsTab.getTabAt(position));
            }
        });
    }

    public void toProfile(View v){
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}