package com.websoftquality.vegimate;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        ViewPager viewPager = findViewById(R.id.viewPager);

//        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));


        TabLayout tableLayout = findViewById(R.id.tableLayout);


        tableLayout.setupWithViewPager(viewPager);


        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("WhatsApp");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home,menu);

        super.onCreateOptionsMenu(menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if(item.getItemId()==R.id.menuSignout){
//            FirebaseAuth.getInstance().signOut();
//
//            finish();
//        }

        return super.onOptionsItemSelected(item);
    }

    private void status(final String status){

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//
//        FirebaseDatabase.getInstance().getReference("Users")
//                .child(user.getUid()).child("status").setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        Log.e("online",status);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("online",status);
//                    }
//                });
    }

    @Override
    protected void onResume() {
        status("online");
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        status("offline");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        status("offline");
        super.onStop();
    }
}
