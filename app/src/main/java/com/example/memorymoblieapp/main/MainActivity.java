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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.memorymoblieapp.R;

import com.example.memorymoblieapp.fragment.AlbumFragment2;
import com.example.memorymoblieapp.fragment.ImageFragment2;
import com.example.memorymoblieapp.obj.Album;
import com.example.memorymoblieapp.obj.Image;
import com.example.memorymoblieapp.view.ViewEdit;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private Button btnViewEdit;
    public static boolean detailed; // view option of image fragment
    public ArrayList<Album> albumList;
    ArrayList<Image> imageList;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detailed = false;
        albumList = new ArrayList<Album>();
        addAlbumList();
        imageList = new ArrayList<Image>();
        addImageList();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.image:
                        Toast.makeText(getBaseContext(), "Image", Toast.LENGTH_LONG).show();
                        // fragmentTransaction.replace(...).commit();
                        fragmentTransaction.addToBackStack("image");
                        return true;

                    case R.id.album:
                        AlbumFragment2 albumFragment = new AlbumFragment2(albumList);
                        fragmentTransaction.replace(R.id.frame_layout_content, albumFragment).commit();
                        fragmentTransaction.addToBackStack("album");
                        return true;

                    case R.id.love:
                        ImageFragment2 imageFragment = new ImageFragment2(imageList, "Yêu thích");
                        fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
                        fragmentTransaction.addToBackStack("love");
                        return true;

                    case R.id.more:
                        Toast.makeText(getBaseContext(), "More", Toast.LENGTH_LONG).show();
                        // fragmentTransaction.replace(...).commit();
                        fragmentTransaction.addToBackStack("more");
                        return true;
                }

                return false;
            }
        });

        btnViewEdit = findViewById(R.id.btnViewEdit);

        btnViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewEdit.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();

        if (backStackEntryCount >= 2) {
            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(backStackEntryCount - 2);
            String fragmentName = backStackEntry.getName();
            if (fragmentName != null) {
                switch (fragmentName) {
                    case "image":
                        bottomNavigationView.getMenu().findItem(R.id.image).setChecked(true);
                        break;
                    case "album":
                        bottomNavigationView.getMenu().findItem(R.id.album).setChecked(true);
                        break;
                    case "love":
                        bottomNavigationView.getMenu().findItem(R.id.love).setChecked(true);
                        break;
                    case "more":
                        bottomNavigationView.getMenu().findItem(R.id.more).setChecked(true);
                        break;
                }
            }
        }

        if (fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStack();
        else
            super.onBackPressed();
    }

    private void addAlbumList() {
        ArrayList<Image> imgList = new ArrayList<Image>();
        imgList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imgList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imgList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imgList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imgList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        albumList.add(new Album("Album1", new ArrayList<Image>(), R.drawable.image1));
        albumList.add(new Album("Album2", imgList, R.drawable.image1));
    }

    private void addImageList() {
        imageList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image2.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image3.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image4.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image5.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image6.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image7.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image8.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image9.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image10.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image11.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image12.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imageList.add(new Image("image13.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
    }
}