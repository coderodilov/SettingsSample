package uz.coderodilov.newyear;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

import com.skydoves.colorpickerview.ColorPickerView;

import uz.coderodilov.newyear.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Dialog settingsDialog;
    private boolean snow, santa, moon, tree, music;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private MediaPlayer player;
    private AudioManager audioManager;
    private int maxVol, currentVol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        editor = preferences.edit();

        settingsDialog = new Dialog(this);
        settingsDialog.setCancelable(false);

        loadSettings();
        soundPlayer();
        playAnimations();

        binding.btnSettings.setOnClickListener(v -> settingsDialog());

    }

    //region MediaPlayer
    private void soundPlayer() {
        if (player == null){
            player = MediaPlayer.create(getApplicationContext(), R.raw.music);
        }
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    private void pausePlayer(){
        if (player.isPlaying()){
            player.pause();
        }
    }

    private void startPlayer(){
        player.start();
    }
    //endregion

    //region MainCode
    private void playAnimations() {
        if (snow){
            binding.lottieSnow.setVisibility(View.VISIBLE);
            binding.lottieSnow.playAnimation();
        } else {
            binding.lottieSnow.setVisibility(View.GONE);
            binding.lottieSnow.cancelAnimation();
        }

        if (santa){
            binding.lottieSanta.setVisibility(View.VISIBLE);
            binding.lottieSanta.playAnimation();
        } else {
            binding.lottieSanta.setVisibility(View.GONE);
            binding.lottieSanta.cancelAnimation();
        }

        if (moon){
            binding.lottieMoon.setVisibility(View.VISIBLE);
            binding.lottieMoon.playAnimation();
        } else {
            binding.lottieMoon.setVisibility(View.GONE);
            binding.lottieMoon.cancelAnimation();
        }

        if (tree){
            binding.lottieTree.setVisibility(View.VISIBLE);
            binding.lottieTree.playAnimation();
        } else {
            binding.lottieTree.setVisibility(View.GONE);
            binding.lottieTree.cancelAnimation();
        }
        if (music){
            startPlayer();
        } else {
            pausePlayer();
        }
    }

    private void loadSettings() {
        snow = preferences.getBoolean("snow", true);
        moon = preferences.getBoolean("moon", true);
        santa = preferences.getBoolean("santa", true);
        tree = preferences.getBoolean("tree", true);
        music = preferences.getBoolean("music", true);
        currentVol = preferences.getInt("volume",0);
    }

    private void settingsDialog() {
        settingsDialog.setContentView(R.layout.settings_dialog);
        Button saveChangesBtn = settingsDialog.findViewById(R.id.btnSaveSettings);
        Button backgroundChangerBtn = settingsDialog.findViewById(R.id.setBgColor);
        Button textColorChanger = settingsDialog.findViewById(R.id.setTextColor);
        ColorPickerView colorPicker = settingsDialog.findViewById(R.id.colorPicker);
        SeekBar musicVolume = settingsDialog.findViewById(R.id.seekbarMusicVolume);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch lottyMoon = settingsDialog.findViewById(R.id.switchMoon);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch lottySanta = settingsDialog.findViewById(R.id.switchSanta);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch lottySnow = settingsDialog.findViewById(R.id.switchSnow);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch lottyTree = settingsDialog.findViewById(R.id.switchTree);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch musicSwitch = settingsDialog.findViewById(R.id.switchMusic);

        settingsDialog.show();

        lottySnow.setChecked(snow);
        lottyMoon.setChecked(moon);
        lottySanta.setChecked(santa);
        lottyTree.setChecked(tree);
        musicSwitch.setChecked(music);

        musicVolume.setMax(maxVol);
        musicVolume.setProgress(currentVol);

        musicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("volume",musicVolume.getProgress()).apply();
            }
        });


        lottySnow.setOnClickListener(v -> {
            if (lottySnow.isChecked()){
                editor.putBoolean("snow", true).apply();
            } else editor.putBoolean("snow", false).apply();
        });

        lottyMoon.setOnClickListener(v -> {
            if (lottyMoon.isChecked()){
                editor.putBoolean("moon", true).apply();
            } else editor.putBoolean("moon", false).apply();
        });

        lottySanta.setOnClickListener(v -> {
            if (lottySanta.isChecked()){
                editor.putBoolean("santa", true).apply();
            } else editor.putBoolean("santa", false).apply();
        });

        lottyTree.setOnClickListener(v -> {
            if (lottyTree.isChecked()){
                editor.putBoolean("tree", true).apply();
            } else  editor.putBoolean("tree", false).apply();
        });

        musicSwitch.setOnClickListener(v -> {
            if (musicSwitch.isChecked()){
                editor.putBoolean("music", true).apply();
            } else editor.putBoolean("music", false).apply();
        });


        backgroundChangerBtn.setOnClickListener(v -> binding.textBackground.setBackgroundColor(colorPicker.getColor()));

        textColorChanger.setOnClickListener(v -> binding.textNewYear.setTextColor(colorPicker.getColor()));

        saveChangesBtn.setOnClickListener(v -> {
            loadSettings();
            playAnimations();
            settingsDialog.dismiss();
        });
    }
    //endregion

    //region LifeCycle
    @Override
    protected void onStart() {
        super.onStart();
        if (music){
            startPlayer();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        pausePlayer();
    }
    //endregion
}