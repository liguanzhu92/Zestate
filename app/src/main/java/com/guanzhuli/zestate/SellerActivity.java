package com.guanzhuli.zestate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.guanzhuli.zestate.realtor.SellerHomeFragment;

public class SellerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        if(findViewById(R.id.seller_activity_container) != null) {
            SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.seller_activity_container, sellerHomeFragment).commit();
        }
    }
}
