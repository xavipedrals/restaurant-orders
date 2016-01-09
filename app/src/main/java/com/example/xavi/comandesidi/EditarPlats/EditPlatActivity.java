package com.example.xavi.comandesidi.EditarPlats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.opengl.ETC1;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
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
import com.example.xavi.comandesidi.NovaComanda.InfoDialog;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.data.GestorBD;
import com.example.xavi.comandesidi.domini.ProductsContainer;

import java.io.IOException;

public class EditPlatActivity extends AppCompatActivity {

    private static final String TAG = "EditPlatActivity";
    private static final int PICK_PHOTO = 11;
    private TextView nameTv, priceTv;
    private Button saveBtn, deleteBtn;
    private boolean specialBackPressed;
    private ImageView image, iconEditName, iconEditPrice;
    private Uri imageUri;
    private String imageUriOriginal;
    private int productId;
    private EditText nameEt, priceEt;
    private int editingState;
    private static int NOT_EDITING = 0;
    private static int EDITING_NAME = 1;
    private static int EDITING_PRICE = 2;


    private CollapsingToolbarLayout collapsingToolbarLayout;

    private void makeEditable(boolean isEditable,EditText et){
        if(isEditable){
            //et.setBackgroundDrawable("Give the textbox background here");//You can store it in some variable and use it over here while making non editable.
            et.setFocusable(true);
            et.setEnabled(true);
            et.setClickable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            //et.setKeyListener("Set edit text key listener here"); //You can store it in some variable and use it over here while making non editable.
        }else{
            //et.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            et.setFocusable(false);
            et.setClickable(false);
            et.setFocusableInTouchMode(false);
            et.setEnabled(false);
            et.setTextColor(getResources().getColor(R.color.primary_text));
            //et.setKeyListener(null);
        }
    }

//    void visualitzaDialog(final boolean isEditName){
//        EditTextDialog editTextDialog = new EditTextDialog();
//        editTextDialog.setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, 0);
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("isEditName", isEditName);
//        editTextDialog.setArguments(bundle);
//        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//        editTextDialog.setOnEditTextDialogResultListener(new EditTextDialog.OnEditTextDialogResultListener() {
//            @Override
//            public void onPositiveResult(String result) {
//                if(isEditName) nameTv.setText(result);
//                else {
//                    priceTv.setText(result);
//                }
//            }
//
//            @Override
//            public void onNegativeResult() {
//
//            }
//        });
//        editTextDialog.show(fragmentManager, "tag");
//    }

    @SuppressWarnings("ConstantConditions")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plat);
        specialBackPressed = false;
        imageUri = null;

        Bundle b = getIntent().getExtras();
        final String name = b.getString("name");
        final double price = b.getDouble("price");
        final int mipmapId = b.getInt("mipmap");
        productId = b.getInt("id");
        boolean hasImage = b.getBoolean("hasImage");
        imageUriOriginal = b.getString("image");

        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
//            actionbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
            actionbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

            actionbar.setTitle("Editar plat");
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    //NavUtils.navigateUpFromSameTask(EditPlatActivity.this);
                }
            });
        }

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        image = (ImageView) findViewById(R.id.image);
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

        priceEt = (EditText) findViewById(R.id.priceEditText);
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

        nameEt = (EditText) findViewById(R.id.nameEditText);
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

        ImageView priceIcon = (ImageView) findViewById(R.id.iconPrice);
        priceIcon.setAlpha(138);

        ImageView foodIcon = (ImageView) findViewById(R.id.iconFood);
        foodIcon.setAlpha(138);

        iconEditName = (ImageView) findViewById(R.id.iconEditName);
        iconEditPrice = (ImageView) findViewById(R.id.iconEditPrice);
        iconEditName.setAlpha(138);
        iconEditPrice.setAlpha(138);
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

        saveBtn = (Button) findViewById(R.id.buttonSave);
        deleteBtn = (Button) findViewById(R.id.buttonDelete);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameAux = nameEt.getText().toString();
                double priceAux = Double.parseDouble(priceEt.getText().toString());
                if(!nameAux.equals(name) || price != priceAux || imageUri != null){
                    if (imageUri != null){
                        GestorBD.getInstance(getApplicationContext()).updatePlat(productId, 1, imageUri.toString(), priceAux, nameAux);
                    } else GestorBD.getInstance(getApplicationContext()).updatePlat(productId, 0, null, priceAux, nameAux);
                    ProductsContainer.refresh(getApplicationContext());
                    specialBackPressed = true;
                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), "Fes algun canvi abans de guardar", Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("Type", "Deleting confirmation");
                InfoDialog infoDialog = new InfoDialog();
                infoDialog.setOnInfoDialogDialogResultListener(new InfoDialog.OnInfoDialogDialogResultListener() {
                    @Override
                    public void onPositiveResult() {
                        GestorBD.getInstance(getApplicationContext()).deletePlat(name);
                        ProductsContainer.refresh(getApplicationContext());
                        specialBackPressed = true;
                        onBackPressed();
                        Toast.makeText(getApplicationContext(), "Producte esborrat", Toast.LENGTH_LONG).show();
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
            Bundle bundle = new Bundle();
            bundle.putInt("Fragment", MainActivity.EDITAR_PLATS_FRAGMENT);
            Intent intent = new Intent(EditPlatActivity.this, MainActivity.class);
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
