//package com.example.memorymoblieapp.main;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentManager;
//
//import android.app.FragmentTransaction;
//import androidx.fragment.app.Fragment;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import com.example.memorymoblieapp.R;
//
//import android.widget.GridView;
//import android.annotation.SuppressLint;
//import com.example.memorymoblieapp.fragment.ImageFragment;
//import com.example.memorymoblieapp.fragment.TitleContentContainerFragment;
//import com.example.memorymoblieapp.view.ViewEdit;
//
//import com.example.memorymoblieapp.databinding.ActivityMainBinding;
//import com.example.memorymoblieapp.fragment.AlbumFragment;
//import com.example.memorymoblieapp.fragment.ImageFragment;
//import com.example.memorymoblieapp.fragment.LoveFragment;
//
//
//public class MainActivity extends AppCompatActivity {
//    private Button btnViewEdit;
//
//    ActivityMainBinding binding;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_main);
//
//        // FragmentAlbum
////        AlbumFragment albumFragment = new AlbumFragment();
////        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, albumFragment).commit();
//        replaceFragment(new ImageFragment());
//        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
//
//            switch(item.getItemId()) {
//                case R.id.image:
//                    replaceFragment(new ImageFragment());
//                    break;
//                case R.id.album:
//                    replaceFragment(new AlbumFragment());
//                    break;
//                case R.id.love:
//                    replaceFragment(new LoveFragment());
//                    break;
//            }
//            return true;
//        });
//
//        ImageFragment imageFragment = new ImageFragment(true);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, imageFragment).commit();
//
//        TitleContentContainerFragment titleContentContainerFragment = new TitleContentContainerFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_title_content_container, titleContentContainerFragment).commit();
//
//
//        btnViewEdit = findViewById(R.id.btnViewEdit);
//
//        btnViewEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ViewEdit.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void replaceFragment(Fragment fragment)
//    {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction  =  fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout_navigation, fragment);
//        fragmentTransaction.commit();
//
//    }
//}


// ***********************************************


package com.example.memorymoblieapp.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.memorymoblieapp.R;

import com.example.memorymoblieapp.fragment.AlbumFragment2;
import com.example.memorymoblieapp.fragment.ImageFragment2;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private Button btnViewEdit;
    public static boolean detailed; // view option of image fragment

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameLayoutNavigation = (FrameLayout) findViewById(R.id.frame_layout_navigation);
        detailed = false;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                if (item.getItemId() != R.id.love)
//                    frameLayoutNavigation.setVisibility(View.GONE);

                switch (item.getItemId()) {
                    case R.id.image:
                        Toast.makeText(getBaseContext(), "Image", Toast.LENGTH_LONG).show();
                        return true;

                    case R.id.album:
                        AlbumFragment2 albumFragment = new AlbumFragment2();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_content, albumFragment).commit();
                        return true;

                    case R.id.love:
//                        frameLayoutNavigation.setVisibility(View.VISIBLE);
//                        TitleContentContainerFragment titleContentContainerFragment = new TitleContentContainerFragment(detailed, "Yêu thích");
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_navigation, titleContentContainerFragment).commit();

                        ImageFragment2 imageFragment = new ImageFragment2("Yêu thích");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_content, imageFragment).commit();
                        return true;

                    case R.id.more:
                        Toast.makeText(getBaseContext(), "More", Toast.LENGTH_LONG).show();
                        return true;
                }

                return false;
            }
        });

        // FragmentAlbum
//        AlbumFragment2 albumFragment = new AlbumFragment2();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, albumFragment).commit();

//        ImageFragment2 imageFragment = new ImageFragment2(true);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, imageFragment).commit();
//
//        TitleContentContainerFragment titleContentContainerFragment = new TitleContentContainerFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_title_content_container, titleContentContainerFragment).commit();


//        btnViewEdit = findViewById(R.id.btnViewEdit);
//
//        btnViewEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ViewEdit.class);
//                startActivity(intent);
//            }
//        });
    }
}