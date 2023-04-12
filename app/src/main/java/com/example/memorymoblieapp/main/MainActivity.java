package com.example.memorymoblieapp.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.memorymoblieapp.DownloadImageFromURL;
import com.example.memorymoblieapp.ImagesGallery;
import com.example.memorymoblieapp.R;

import com.example.memorymoblieapp.adapter.GalleryAdapter;
import com.example.memorymoblieapp.adapter.ImageAdapter;
import com.example.memorymoblieapp.databinding.ActivityMainBinding;
import com.example.memorymoblieapp.fragment.AlbumFragment2;
import com.example.memorymoblieapp.fragment.ImageFragment;
import com.example.memorymoblieapp.fragment.ImageFragment2;

import com.example.memorymoblieapp.fragment.UrlDialog;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.fragment.SettingsFragment;
import com.example.memorymoblieapp.obj.Album;
import com.example.memorymoblieapp.view.ViewSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ImageFragment imageFragment;
    FragmentTransaction fragmentTransaction;
    public static boolean detailed; // view option of image fragment
    public static ArrayList<Album> albumList;
    public static ArrayList<String> lovedImageList;
    public static ArrayList<String> deletedImageList;
    static BottomNavigationView bottomNavigationView;
    private static final int PERMISSION_REQUEST_CODE = 200;
    //    private ArrayList<String> imagePaths = new ArrayList<String>();
    private RecyclerView recyclerView;
    private static List<String> imageDates;
    static ArrayList<String> images;
    static ArrayList<String> newImage;
    static ArrayList<String> trashListImage;
    GalleryAdapter galleryAdapter;
    boolean isPermission = false;
    private static final int MY_READ_PERMISSION_CODE = 101;
    public static boolean isVerify = false; // Status of album blocking
    @SuppressLint("StaticFieldLeak")
    static FrameLayout frame_layout_selection_features_bar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String[] permissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET};

        if (!checkPermissionList(permissionList))
            ActivityCompat.requestPermissions(MainActivity.this, permissionList, 1);
        else initiateApp();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            initiateApp();
        }
    }

    private boolean checkPermissionList(String[] permissionList) {
        int permissionCheck;
        for (String per : permissionList) {
            permissionCheck = ContextCompat.checkSelfPermission(this, per);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) return false;
        }
        return true;
    }

    public static void updateData(Context context) {
        imageDates = new ArrayList<>();

        trashListImage = DataLocalManager.getStringList(KeyData.TRASH_LIST.getKey());

        images = ImagesGallery.listOfImages(context);
        newImage = handleSortListImageView();
        ArrayList<String> picturePath = new ArrayList<>(newImage);

        picturePath.removeAll(Collections.singleton(" "));

        DataLocalManager.saveData(KeyData.IMAGE_PATH_VIEW_LIST.getKey(), newImage);
        DataLocalManager.saveData(KeyData.IMAGE_PATH_LIST.getKey(), picturePath);
    }

    @SuppressLint("NonConstantResourceId")
    private void initiateApp() {
        imageDates = new ArrayList<>();

        trashListImage = DataLocalManager.getStringList(KeyData.TRASH_LIST.getKey());

        images = ImagesGallery.listOfImages(this);
        newImage = handleSortListImageView();
        ArrayList<String> picturePath = new ArrayList<>(newImage);

        picturePath.removeAll(Collections.singleton(" "));

        DataLocalManager.saveData(KeyData.IMAGE_PATH_VIEW_LIST.getKey(), newImage);
        DataLocalManager.saveData(KeyData.IMAGE_PATH_LIST.getKey(), picturePath);

        detailed = false;
        albumList = new ArrayList<>(DataLocalManager.getObjectList(KeyData.ALBUM_DATA_LIST.getKey(), Album.class));
        lovedImageList = DataLocalManager.getStringList(KeyData.FAVORITE_LIST.getKey());
        lovedImageList = lovedImageList == null ? new ArrayList<>() : lovedImageList;
        deletedImageList = DataLocalManager.getStringList(KeyData.TRASH_LIST.getKey());
        deletedImageList = deletedImageList == null ? new ArrayList<>() : deletedImageList;

        frame_layout_selection_features_bar = findViewById(R.id.frame_layout_selection_features_bar);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        imageFragment = new ImageFragment(newImage, imageDates);
        fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
        fragmentTransaction.addToBackStack("image");

        Intent intent = getIntent();
        String request = intent.getStringExtra("request");
        String albumName = intent.getStringExtra("album_name");
        if (request != null && albumName != null) {
            onMsgToMain(albumName, request);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.image:
                    newImage.clear();
                    imageDates.clear();
                    newImage = handleSortListImageView();
                    ImageFragment imageFragment1 = new ImageFragment(newImage, imageDates);

                    fragmentTransaction1.replace(R.id.frame_layout_content, imageFragment1).commit();
                    fragmentTransaction1.addToBackStack("image");
                    return true;


                case R.id.album:
                    AlbumFragment2 albumFragment = new AlbumFragment2(albumList);
                    fragmentTransaction.replace(R.id.frame_layout_content, albumFragment).commit();
                    fragmentTransaction.addToBackStack("album");
                    return true;

                case R.id.love:
                    ImageFragment2 loveImageFragment = new ImageFragment2(lovedImageList, "Yêu thích", "Love");
                    fragmentTransaction.replace(R.id.frame_layout_content, loveImageFragment).commit();
                    fragmentTransaction.addToBackStack("love");
                    return true;

                case R.id.more:
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, bottomNavigationView, Gravity.END);
                    popupMenu.inflate(R.menu.bottom_navigation_more_menu);
                    popupMenu.setOnMenuItemClickListener(menuItem -> {
                        int itemId = menuItem.getItemId();
                        if (R.id.recycleBin == itemId) {
                            ImageFragment2 deletedImageFragment = new ImageFragment2(deletedImageList, "Thùng rác", "TrashBin");
                            fragmentTransaction.replace(R.id.frame_layout_content, deletedImageFragment).commit();
                        } else if (R.id.URL == itemId) {
                            Toast.makeText(MainActivity.this, "Tải ảnh bằng URL", Toast.LENGTH_LONG).show();
                        } else if (R.id.settings == itemId) {
                            SettingsFragment settingsFragment = new SettingsFragment();
                            fragmentTransaction.replace(R.id.frame_layout_content, settingsFragment).commit();
                        }
                        return true;
                    });
                    popupMenu.show();
                    fragmentTransaction.addToBackStack("more");

                    return true;
            }
            return false;
        });
    }

    @SuppressLint("SimpleDateFormat")
    public static ArrayList<String> handleSortListImageView() {
        ArrayList<String> newImage = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        trashListImage = DataLocalManager.getStringList(KeyData.TRASH_LIST.getKey());

        int flag = 0;

        for (String imagePath : images) {
            if (imagePath != null && trashListImage.contains(imagePath)==false) {

                File imageFile = new File(imagePath);
                Date imageDate = new Date(imageFile.lastModified());
                if (imageDates.size() != 0 && !dateFormat.format(imageDate).equals(imageDates.get(imageDates.size() - 1))) {

                    if (flag % 3 == 2) {
                        newImage.add(" ");
                        imageDates.add(" ");
                    }
                    if (flag % 3 == 1) {
                        newImage.add(" ");
                        newImage.add(" ");
                        imageDates.add(" ");
                        imageDates.add(" ");
                    }
                    flag = 0;
                }
                newImage.add(imagePath);
                imageDates.add(dateFormat.format(imageDate));
                flag++;
//                Log.d("MyTag", dateFormat.format(imageDate) + imagePath);
            }
        }
        return newImage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionSearch) {
            Intent intent = new Intent(this, ViewSearch.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Toast.makeText(MainActivity.this, "No network is currently active!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!networkInfo.isConnected()) {
            Toast.makeText(MainActivity.this, "Network is not connected!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!networkInfo.isAvailable()) {
            Toast.makeText(MainActivity.this, "Network is not available!", Toast.LENGTH_SHORT).show();
            return false;
        }

        Toast.makeText(MainActivity.this, "Network is OK!", Toast.LENGTH_SHORT).show();
        return true;
    }
    public void onMsgFromFragToMain(String request) {
        boolean network = checkInternetConnection();
        if (!network)
            return;
        DownloadImageFromURL task = new DownloadImageFromURL();
        task.execute(request);
        try {
            Bitmap bitmap = task.get();
            //  picturesFragment.onMsgFromMainToFrag(bitmap);
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myImageFolder");

            // Create the directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String imageName = UUID.randomUUID().toString() + ".jpg";
            // Create a file to save the image
            File file = new File(directory, imageName);
            OutputStream outputStream = new FileOutputStream(file);

            // Compress the bitmap and write it to the output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            // Flush and close the output stream
            outputStream.flush();
            outputStream.close();

            // Add the image to the gallery so it can be viewed in other apps
            MediaScannerConnection.scanFile(MainActivity.this, new String[]{file.getAbsolutePath()}, null, null);
            MediaScannerConnection.scanFile(MainActivity.this, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {
                    // Do nothing
                }

                @Override
                public void onScanCompleted(String path, Uri uri) {
                    newImage.clear();
                    imageDates.clear();
//                    newImage = handleSortListImageView();
                    File imageFile = new File(path);
                    Date imageDate = new Date(imageFile.lastModified());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");

                    Log.d("Taggg",path + dateFormat.format(imageDate));
                }
            });
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, "No result", Toast.LENGTH_SHORT).show();
        }
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
                    case "selectImage":
                        frame_layout_selection_features_bar.removeAllViews();
                        break;
                }
            }
        }

        if (fragmentManager.getBackStackEntryCount() > 0) fragmentManager.popBackStack();
        else super.onBackPressed();

        bottomNavigationView.setVisibility(View.VISIBLE);
        GalleryAdapter.clearListSelect();

        ImageAdapter.ViewHolder.turnOffSelectMode();
    }

    public void onMsgToMain(String data, String request) {
        if (request.equals("VIEW_ALBUM_IMAGE")) {
            int pos = 0;
            for (Album a : albumList) {
                if (!a.getAlbumName().equals(data))
                    pos++;
                else break;
            }

            ImageFragment2 imageFragment = new ImageFragment2(albumList.get(pos).getPathImages(), albumList.get(pos).getAlbumName());
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
            fragmentTransaction.addToBackStack("album");
        }
    }

    public static BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public static FrameLayout getFrameLayoutSelectionFeaturesBar() { return frame_layout_selection_features_bar; }

    public static ArrayList<String> getNewImage() {
        return newImage;
    }

    public static List<String> getImageDates() {
        return imageDates;
    }

    public static ArrayList<String> getImages() { return images; }
}
