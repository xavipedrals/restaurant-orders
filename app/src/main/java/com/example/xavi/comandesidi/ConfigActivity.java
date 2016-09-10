package com.example.xavi.comandesidi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xavi.comandesidi.NewOrder.InfoDialog;
import com.example.xavi.comandesidi.DBManager.DBManager;
import com.example.xavi.comandesidi.DBWrappers.OrderContainer;
import com.example.xavi.comandesidi.DBWrappers.ProductsContainer;

import java.io.IOException;

public class ConfigActivity extends AppCompatActivity {

    private static final String TAG = "ConfigActivity";
    private static final int PICK_PHOTO = 11;
    private EditText nameEt, mailEt;
    private ImageView image, iconEditName, iconEditMail;
    private Button saveBtn, deletePlatsBtn, deleteComandesBtn;
    private String restaurantName, restaurantEmail, backgroundUri;
    private Uri imageUri;
    private boolean specialBackPressed;
    private int editingState;
    private static int NOT_EDITING = 0;
    private static int EDITING_NAME = 1;
    private static int EDITING_MAIL = 2;



    private void makeEditable(boolean isEditable,EditText et){
        if(isEditable){
            et.setFocusable(true);
            et.setEnabled(true);
            et.setClickable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
        } else{
            et.setFocusable(false);
            et.setClickable(false);
            et.setFocusableInTouchMode(false);
            et.setEnabled(false);
            et.setTextColor(getResources().getColor(R.color.primary_text));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracio);

        SharedPreferences prefs = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        restaurantName = prefs.getString("NomRestaurant", getResources().getString(R.string.nav_drawer_title));
        restaurantEmail = prefs.getString("EmailRestaurant", getResources().getString(R.string.nav_drawer_email));
        backgroundUri = prefs.getString("BackgroundUri", "");
        specialBackPressed = false;

        image = (ImageView) findViewById(R.id.image);
        Bitmap bitmap = null;
        if(!backgroundUri.equals("")) {
            Uri uri = Uri.parse(backgroundUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.restaurant2);
        }
        image.setImageBitmap(bitmap);


        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
            actionbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

            actionbar.setTitle("Configuració");
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));


        nameEt = (EditText) findViewById(R.id.nameEditText);
        nameEt.setText(restaurantName);
        nameEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    iconEditName.setImageResource(R.mipmap.ic_edit_black_48dp);
                    makeEditable(false, nameEt);
                }
                return false;
            }
        });
        makeEditable(false, nameEt);

        mailEt = (EditText) findViewById(R.id.mailEditText);
        mailEt.setText(restaurantEmail);
        mailEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    iconEditMail.setImageResource(R.mipmap.ic_edit_black_48dp);
                    makeEditable(false, mailEt);
                }
                return false;
            }
        });
        makeEditable(false, mailEt);


        ImageView iconName = (ImageView) findViewById(R.id.iconName);
        ImageView iconEmail = (ImageView) findViewById(R.id.iconEmail);
        iconName.setAlpha(138);
        iconEmail.setAlpha(138);

        iconEditName = (ImageView) findViewById(R.id.iconEditName);
        iconEditName.setAlpha(138);
        iconEditName.setClickable(true);
        editingState = NOT_EDITING;
        iconEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editingState == EDITING_NAME) {
                    makeEditable(false, nameEt);
                    iconEditName.setImageResource(R.mipmap.ic_edit_black_48dp);
                    editingState = NOT_EDITING;
                } else {
                    makeEditable(true, nameEt);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(nameEt, InputMethodManager.SHOW_IMPLICIT);
                    iconEditName.setImageResource(R.mipmap.ic_done_black_48dp);
                    editingState = EDITING_NAME;
                }
            }
        });

        iconEditMail = (ImageView) findViewById(R.id.iconEditMail);
        iconEditMail.setAlpha(138);
        iconEditMail.setClickable(true);
        iconEditMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editingState == EDITING_MAIL) {
                    makeEditable(false, mailEt);
                    iconEditMail.setImageResource(R.mipmap.ic_edit_black_48dp);
                    editingState = NOT_EDITING;
                } else {
                    makeEditable(true, mailEt);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mailEt, InputMethodManager.SHOW_IMPLICIT);
                    iconEditMail.setImageResource(R.mipmap.ic_done_black_48dp);
                    editingState = EDITING_MAIL;
                }
            }
        });

        saveBtn = (Button) findViewById(R.id.buttonSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameAux = nameEt.getText().toString();
                String emailAux = mailEt.getText().toString();
                SharedPreferences prefs = getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
                boolean hasChanges = false;
                if(!nameAux.equals(restaurantName)) {
                    prefs.edit().putString("NomRestaurant", nameAux).apply();
                    hasChanges = true;
                }
                if(!emailAux.equals(restaurantEmail)) {
                    prefs.edit().putString("EmailRestaurant", emailAux).apply();
                    hasChanges = true;
                }
                if(imageUri != null) {
                    prefs.edit().putString("BackgroundUri", imageUri.toString()).apply();
                    hasChanges = true;
                }
                if (!hasChanges){
                    Toast.makeText(getApplicationContext(), "Fes algun canvi abans de guardar", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Els canvis s'han guardat", Toast.LENGTH_LONG).show();
                    specialBackPressed = true;
                    onBackPressed();
                }
            }
        });
        deletePlatsBtn = (Button) findViewById(R.id.buttonDeletePlats);
        deletePlatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("Type", "Reset table plats");
                InfoDialog infoDialog = new InfoDialog();
                infoDialog.setOnInfoDialogDialogResultListener(new InfoDialog.OnInfoDialogDialogResultListener() {
                    @Override
                    public void onPositiveResult() {
                        DBManager.getInstance(getApplicationContext()).resetTablePlats();
                        ProductsContainer.refreshAfterReset(getApplicationContext());
                        specialBackPressed = true;
                        onBackPressed();
                        Toast.makeText(getApplicationContext(), "S'han esborrat els plats", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onNegativeResult() {

                    }
                });
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                infoDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                infoDialog.setArguments(b);
                infoDialog.show(fragmentManager, "tag");
            }
        });

        deleteComandesBtn = (Button) findViewById(R.id.buttonDeleteComandes);
        deleteComandesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("Type", "Reset table comandes");
                InfoDialog infoDialog = new InfoDialog();
                infoDialog.setOnInfoDialogDialogResultListener(new InfoDialog.OnInfoDialogDialogResultListener() {
                    @Override
                    public void onPositiveResult() {
                        DBManager.getInstance(getApplicationContext()).resetTableComandes();
                        OrderContainer.refreshAfterReset(getApplicationContext());
                        specialBackPressed = true;
                        onBackPressed();
                        Toast.makeText(getApplicationContext(), "S'han esborrat les comandes", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onNegativeResult() {

                    }
                });
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                infoDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                infoDialog.setArguments(b);
                infoDialog.show(fragmentManager, "tag");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Escull una imatge"), PICK_PHOTO);
            }
        });
    }

    @Override public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "Error al carregar la imatge", Toast.LENGTH_LONG).show();
                return;
            }
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }

    @Override
    public void onBackPressed() {
        if(specialBackPressed){
            Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        super.onBackPressed();
    }

}
