package com.example.xavi.comandesidi.EditarPlats;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xavi.comandesidi.MainActivity;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.data.GestorBD;
import com.example.xavi.comandesidi.domini.ProductsContainer;

public class EditPlatActivity extends AppCompatActivity {

    private static final String TAG = "EditPlatActivity";
    private TextView nameTv, priceTv;
    private Button saveBtn, deleteBtn;
    private boolean specialBackPressed;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    void visualitzaDialog(final boolean isEditName){
        EditTextDialog editTextDialog = new EditTextDialog();
        editTextDialog.setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, 0);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isEditName", isEditName);
        editTextDialog.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        editTextDialog.setOnEditTextDialogResultListener(new EditTextDialog.OnEditTextDialogResultListener() {
            @Override
            public void onPositiveResult(String result) {
                if(isEditName) nameTv.setText(result);
                else {
                    priceTv.setText(result);
                }
            }

            @Override
            public void onNegativeResult() {

            }
        });
        editTextDialog.show(fragmentManager, "tag");
    }

    @SuppressWarnings("ConstantConditions")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plat);
        specialBackPressed = false;

        Bundle b = getIntent().getExtras();
        final String name = b.getString("name");
        final double price = b.getDouble("price");
        final int mipmapId = b.getInt("mipmap");

        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
//            actionbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
            actionbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

            actionbar.setTitle(name);
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    //NavUtils.navigateUpFromSameTask(EditPlatActivity.this);
                }
            });
        }

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle(itemTitle);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        final ImageView image = (ImageView) findViewById(R.id.image);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mipmapId);
        image.setImageBitmap(bitmap);

        priceTv = (TextView) findViewById(R.id.priceTextView);
        String aux = String.valueOf(price);
        priceTv.setText(aux);

        nameTv = (TextView) findViewById(R.id.nameTextView);
        nameTv.setText(name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Seleccionar una nova imatge
            }
        });

        ImageView priceIcon = (ImageView) findViewById(R.id.iconPrice);
        priceIcon.setAlpha(138);

        ImageView foodIcon = (ImageView) findViewById(R.id.iconFood);
        foodIcon.setAlpha(138);

        ImageView iconEditName = (ImageView) findViewById(R.id.iconEditName);
        ImageView iconEditPrice = (ImageView) findViewById(R.id.iconEditPrice);
        iconEditName.setAlpha(138);
        iconEditPrice.setAlpha(138);
        iconEditName.setClickable(true);
        iconEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualitzaDialog(true);
            }
        });
        iconEditPrice.setClickable(true);
        iconEditPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualitzaDialog(false);
            }
        });

        saveBtn = (Button) findViewById(R.id.buttonSave);
        deleteBtn = (Button) findViewById(R.id.buttonDelete);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameAux = nameTv.getText().toString();
                double priceAux = Double.parseDouble(priceTv.getText().toString());
                if(!nameAux.equals(name) || price != priceAux){
                    GestorBD.getInstance(getApplicationContext()).updatePlat(mipmapId, priceAux, nameAux, name);
                    ProductsContainer.refresh(getApplicationContext());
                    specialBackPressed = true;
                    onBackPressed();
                }
                //GestorBD.getInstance(getApplicationContext())
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Afegir comprovació extra
//                GestorBD.getInstance(getApplicationContext()).deletePlat(name);
//                ProductsContainer.refresh(getApplicationContext());
//                specialBackPressed = true;
//                onBackPressed();
            }
        });
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