package com.example.budget;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuickAddActivity extends AppCompatActivity {
    public Long selectedEnvelopeID;
    public int selectedAccountID;
    public EditText payee;
    public Float accountBalance, envelopeBalance;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add);

        final Spinner envelopeSpinner = findViewById(R.id.quickAddSelectEnvelopeSpinner);
        final Spinner accountSpinner = findViewById(R.id.quickAddSelectAccountSpinner);
        final EditText amount = findViewById(R.id.quickAddAmountET);
        payee = findViewById(R.id.quickAddPayeeET);
        db = new DatabaseHelper(this);
        Cursor envelopes = db.getAllEnvelopeData();

        final ArrayList<String> envelopeAL = new ArrayList<String>();
        ArrayList<String> accountAL = new ArrayList<String>();
        final ArrayList<Integer> accountID_AL = new ArrayList<Integer>();
        final ArrayList<Long> envelopeID_AL = new ArrayList<Long>();
        final ArrayList<Float> accountBalance_AL = new ArrayList<Float>();
        final ArrayList<Float> envelopeBalance_AL = new ArrayList<Float>();
        if (envelopes.getCount() != 0) {
            while (envelopes.moveToNext()){
                envelopeAL.add(envelopes.getString(1).toUpperCase());
                envelopeID_AL.add(envelopes.getLong(0));
                envelopeBalance_AL.add(envelopes.getFloat(2));
            }
        }
        Cursor accounts = db.getAllAccounts();

        if (accounts.getCount() != 0) {
            while (accounts.moveToNext()) {
                accountAL.add(accounts.getString(1).toUpperCase());
                accountID_AL.add(accounts.getInt(0));
                accountBalance_AL.add(accounts.getFloat(2));
            }
        }

        final ArrayAdapter<String> envelopeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, envelopeAL);
        envelopeSpinner.setAdapter(envelopeAdapter);

        ArrayAdapter<String> accountAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, accountAL);
        accountSpinner.setAdapter(accountAdapter);

        Button mainMenu = findViewById(R.id.quickAddMainMenuButton);
        Button submit = findViewById(R.id.quickAddSubmitButton);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuickAddActivity.this, MainActivity.class));
            }
        });

        envelopeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEnvelopeID = envelopeID_AL.get(position);
                envelopeBalance = envelopeBalance_AL.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAccountID = accountID_AL.get(position);
                accountBalance = accountBalance_AL.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
                String date = sdf.format(new Date());
                Float enteredAmount = Float.parseFloat(amount.getText().toString());
                enteredAmount = -enteredAmount;
                db.insertRegisterData(selectedEnvelopeID, date, payee.getText().toString(), enteredAmount, enteredAmount);


                accountBalance += enteredAmount;
                envelopeBalance += enteredAmount;

                db.updateAccountBalance(selectedAccountID, accountBalance);
                db.updateEnvelopeBalance(selectedEnvelopeID, envelopeBalance);

                startActivity(new Intent(QuickAddActivity.this, MainActivity.class));

            }
        });

    }
}
