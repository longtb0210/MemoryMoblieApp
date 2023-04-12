package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    TextView txtAlbumBlock;
    Spinner spinnerLanguage;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchDarkMode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View settingsFragment = inflater.inflate(R.layout.settings_fragment, container, false);
        Context context = settingsFragment.getContext();
        txtAlbumBlock = settingsFragment.findViewById(R.id.txtAlbumBlock);
        spinnerLanguage = settingsFragment.findViewById(R.id.spinnerLanguage);
        switchDarkMode = settingsFragment.findViewById(R.id.switchDarkMode);

        // Album block
        txtAlbumBlock.setOnClickListener(view -> {
            AlbumBlockFragment albumBlockFragment = new AlbumBlockFragment();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_content, albumBlockFragment).commit();
            fragmentTransaction.addToBackStack("more");
        });

        // Language
        ArrayList<String> languages = new ArrayList<>();
        languages.add("Tiếng Việt");
        languages.add("Tiếng Anh");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, languages);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(arrayAdapter);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Toast.makeText(context, languages.get(pos), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Dark mode
        Boolean isThemeDark = DataLocalManager.getBooleanData(KeyData.DARK_MODE.getKey());
        isThemeDark = isThemeDark == null ? false : isThemeDark;

        getActivity().setTheme(isThemeDark ? R.style.ThemeDark_MemoryMobileApp : R.style.Theme_MemoryMobileApp);
        switchDarkMode.setChecked(isThemeDark);
        switchDarkMode.setOnCheckedChangeListener((compoundButton, darkModeOn) -> {
            if (darkModeOn)
                Toast.makeText(context, "Bật chế độ tối", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "Tắt chế độ tối", Toast.LENGTH_SHORT).show();
        });

        return settingsFragment;
    }
}