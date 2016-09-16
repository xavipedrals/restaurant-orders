package com.example.xavi.comandesidi.EditDish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.example.xavi.comandesidi.MainActivity;
import com.example.xavi.comandesidi.Utils.InfoDialog;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.DBManager.DBManager;
import com.example.xavi.comandesidi.DBWrappers.DishesContainer;
import com.example.xavi.comandesidi.Utils.ConstantValues;

import java.io.IOException;

public class EditDishActivity extends AppCompatActivity {

    private static final String TAG = "EditPlatActivity";
    private static final int PICK_PHOTO = 11;
    private Button saveBtn, deleteBtn;
    private boolean specialBackPressed;
    private ImageView image, iconEditName, iconEditPrice, priceIcon, foodIcon;
    private Toolbar actionbar;
    private Uri imageUri;
    private FloatingActionButton fab;

    private EditText nameEt, priceEt;
    private int editingState;
    private static int NOT_EDITING = 0;
    private static int EDITING_NAME = 1;
    private static int EDITING_PRICE = 2;

    private String name;
    private double price;
    private int mipmapId;
    private int productId;
    boolean hasImage;
    private String imageUriOriginal;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @SuppressWarnings("ConstantConditions")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plat);
        specialBackPressed = false;
        imageUri = null;
        manageIncomingBundle();
        initVisualItems();
        configActionBar();
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        setDishImage();
        configPriceEditText();
        configNameEditText();
        configIconEditName();
        configIconEditPrice();
        configSaveButton();
        configDeleteButton();
        configFloatingActionButton();
    }

    private void manageIncomingBundle() {
        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        price = b.getDouble("price");
        mipmapId = b.getInt("mipmap");
        productId = b.getInt("id");
        hasImage = b.getBoolean("hasImage");
        imageUriOriginal = b.getString("image");
    }

    private void initVisualItems() {
        actionbar = (Toolbar) findViewById(R.id.toolbar);
        image = (ImageView) findViewById(R.id.image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        priceEt = (EditText) findViewById(R.id.priceEditText);
        nameEt = (EditText) findViewById(R.id.nameEditText);
        priceIcon = (ImageView) findViewById(R.id.iconPrice);
        foodIcon = (ImageView) findViewById(R.id.iconFood);
        iconEditName = (ImageView) findViewById(R.id.iconEditName);
        iconEditPrice = (ImageView) findViewById(R.id.iconEditPrice);
        saveBtn = (Button) findViewById(R.id.buttonSave);
        deleteBtn = (Button) findViewById(R.id.buttonDelete);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setGreyIcons();
    }

    private void setGreyIcons() {
        priceIcon.setAlpha(ConstantValues.alpha);
        foodIcon.setAlpha(ConstantValues.alpha);
        iconEditName.setAlpha(ConstantValues.alpha);
        iconEditPrice.setAlpha(ConstantValues.alpha);
    }

    private void configActionBar() {
        if (null != actionbar) {
            actionbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            actionbar.setTitle("Edit dish");
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void setDishImage() {
        Bitmap bitmap = null;
        if(hasImage) {
            Uri uri = Uri.parse(imageUriOriginal);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), mipmapId);
        }
        image.setImageBitmap(bitmap);
    }

    private void configPriceEditText() {
        String aux = String.valueOf(price);
        priceEt.setText(aux);
        priceEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    iconEditPrice.setImageResource(R.mipmap.ic_edit_black_48dp);
                    makeEditable(false, priceEt);
                }
                return false;
            }
        });
        makeEditable(false, priceEt);
    }

    private void configNameEditText() {
        nameEt.setText(name);
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
    }

    private void configIconEditName() {
        iconEditName.setClickable(true);
        editingState = NOT_EDITING;
        iconEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editingState == EDITING_NAME){
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
    }

    private void configIconEditPrice() {
        iconEditPrice.setClickable(true);
        iconEditPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editingState == EDITING_PRICE){
                    makeEditable(false, priceEt);
                    iconEditPrice.setImageResource(R.mipmap.ic_edit_black_48dp);
                    editingState = NOT_EDITING;
                } else {
                    makeEditable(true, priceEt);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(priceEt, InputMethodManager.SHOW_IMPLICIT);
                    iconEditPrice.setImageResource(R.mipmap.ic_done_black_48dp);
                    editingState = EDITING_PRICE;
                }
            }
        });
    }

    private void configSaveButton() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameAux = nameEt.getText().toString();
                double priceAux = Double.parseDouble(priceEt.getText().toString());
                if(!nameAux.equals(name) || price != priceAux || imageUri != null){
                    if (imageUri != null){
                        DBManager.getInstance(getApplicationContext()).updateDish(productId, 1, imageUri.toString(), priceAux, nameAux);
                    } else DBManager.getInstance(getApplicationContext()).updateDish(productId, 0, null, priceAux, nameAux);
                    DishesContainer.refresh(getApplicationContext());
                    specialBackPressed = true;
                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), "Fes algun canvi abans de guardar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void configDeleteButton() {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("Type", "Deleting confirmation");
                InfoDialog infoDialog = new InfoDialog();
                infoDialog.setOnInfoDialogDialogResultListener(new InfoDialog.OnInfoDialogDialogResultListener() {
                    @Override
                    public void onPositiveResult() {
                        DBManager.getInstance(getApplicationContext()).deleteDish(name);
                        DishesContainer.refresh(getApplicationContext());
                        specialBackPressed = true;
                        onBackPressed();
                        Toast.makeText(getApplicationContext(), "Deleted dish", Toast.LENGTH_LONG).show();
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
    }

    private void configFloatingActionButton() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Pick an image"), PICK_PHOTO);
            }
        });
    }

    private void makeEditable(boolean isEditable,EditText et){
        if(isEditable) {
            et.setFocusable(true);
            et.setEnabled(true);
            et.setClickable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
        }
        else {
            et.setFocusable(false);
            et.setClickable(false);
            et.setFocusableInTouchMode(false);
            et.setEnabled(false);
            et.setTextColor(getResources().getColor(R.color.primary_text));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "Error loading the image", Toast.LENGTH_LONG).show();
                return;
            }
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }

    @Override
    public void onBackPressed() {
        if(specialBackPressed){
            Bundle bundle = new Bundle();
            bundle.putInt("Fragment", MainActivity.EDIT_DISHES_FRAGMENT);
            Intent intent = new Intent(EditDishActivity.this, MainActivity.class);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    @Override public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }


}
