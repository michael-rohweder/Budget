package com.example.budget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ViewEnvelopes extends AppCompatActivity {

    public static LinearLayout layoutHolder;
    public static DatabaseHelper db, registerDB;
    public float totalCash=0.0f;
    public static TextView totalCashTV;
    public static Long id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_envelopes);

        db = new DatabaseHelper(this);
        registerDB = new DatabaseHelper(this);

        layoutHolder = findViewById(R.id.viewEnvelopeEnvelopeHolderLinearLayout);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        Button createEnvelope = findViewById(R.id.viewEnvelopeCreateEnvelopeButton);
        createEnvelope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewEnvelopes.layoutHolder.getContext());
                builder.setTitle("New Envelope");
                final EditText name = new EditText(ViewEnvelopes.layoutHolder.getContext());
                name.setHint("Envelope Name");
                final EditText amount = new EditText(ViewEnvelopes.layoutHolder.getContext());
                amount.setHint("$0.00 - Initial Budget Amount");
                amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                LinearLayout lay = new LinearLayout(ViewEnvelopes.layoutHolder.getContext());
                lay.setOrientation(LinearLayout.VERTICAL);
                lay.addView(name);
                lay.addView(amount);

                builder.setView(lay);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String buttonText = name.getText().toString() + " - $" + amount.getText().toString();

                        String amountB = amount.getText().toString();
                        String nameS = name.getText().toString();
                        Float amountF = Float.parseFloat(amountB);
                        id = ViewEnvelopes.db.insertEnvelopeData(nameS, amountF);
                        if (id != -1) {
                            Button newEnvelope = new Button(layoutHolder.getContext());
                            newEnvelope.setText(buttonText);
                            newEnvelope.setTag(id);
                            newEnvelope.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ViewEnvelopes.this, ViewEnvelopeDetails.class);
                                    intent.putExtra("EXTRA_SESSION_ID", id);
                                    startActivity(intent);
                                }
                            });

                            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
                            String currentDate = sdf.format(new Date());

                            long result = db.insertRegisterData(id,currentDate,"STARTING", amountF, amountF);
                            layoutHolder.removeAllViews();
                            loadDB();
                        }

                        updateTotalTV();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        Button mainMenu = findViewById(R.id.viewEnvelopeMainMenuButton);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewEnvelopes.this, MainActivity.class));
            }
        });
        loadDB();
        totalCashTV = findViewById(R.id.viewEnvelopeTotalCashTV);
        totalCashTV.setText("Total budgeted amount: " + String.format("$%.2f", totalCash));


    }

    public void loadDB(){
        Cursor res = db.getAllEnvelopeData();
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                final Long id = res.getLong(0);
                String name = res.getString(1);
                Float amount = res.getFloat(2);
                String buttonText = name + " - " + String.format("$%.2f", amount);
                totalCash += amount;
                Button newEnvelope = new Button(ViewEnvelopes.layoutHolder.getContext());
                if (amount < 0) {
                    newEnvelope.setTextColor(Color.parseColor("#FF0000"));
                }
                newEnvelope.setText(buttonText);
                newEnvelope.setTag(id);
                newEnvelope.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ViewEnvelopes.this, ViewEnvelopeDetails.class);
                        intent.putExtra("EXTRA_SESSION_ID", id);
                        startActivity(intent);
                    }
                });
                ViewEnvelopes.layoutHolder.addView(newEnvelope);
            }
        }
    }
    public void updateTotalTV(){
        Cursor res = db.getAllEnvelopeData();
        Float totalCash=0.0f;
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                Float amount = res.getFloat(2);
                totalCash += amount;
            }
        }
        ViewEnvelopes.totalCashTV.setText("Total budgeted amount: " + String.format("$%.2f", totalCash));
    }
}
