package com.example.budget;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class envelopeTransfer extends AppCompatActivity {

    public Long passedEnvelopeID, selectedEnvelopeID;
    public String fromEnvelopeName, toEnvelopeName;
    public DatabaseHelper db;
    public Float toEnvelopeBalance, fromEnvelopeBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evenlope_transfer);

        final Spinner toEnvelopeSpinner = findViewById(R.id.envelopeTransferToEnvelopeSpinner);
        TextView fromEnvelope = findViewById(R.id.envelopeTransferFromEnvelopeTV);
        final EditText amount = findViewById(R.id.envelopeTransferAmountET);

        Button mainMenu = findViewById(R.id.envelopeTransferMainMenuButton);
        Button back = findViewById(R.id.envelopeTransferBackButton);
        Button submit = findViewById(R.id.envelopeTransferSubmitButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            passedEnvelopeID = extras.getLong("envelopeID");
        }

        db = new DatabaseHelper(this);
        Cursor envelope = db.getEnvelopeData(passedEnvelopeID);
        if (envelope.getCount() != 0){
            while (envelope.moveToNext()) {
                fromEnvelopeName = envelope.getString(1);
                fromEnvelopeBalance = envelope.getFloat(2);
            }
        }

        String fromEnvelopeText = "Transfer from envelope: " + fromEnvelopeName.toUpperCase() + " - Balance: " + fromEnvelopeBalance;
        fromEnvelope.setText(fromEnvelopeText);

        Cursor allEnvelopes = db.getAllEnvelopeData();
        final ArrayList<String> envelopeName_AL = new ArrayList<String>();
        final ArrayList<Long> envelopeID_AL = new ArrayList<Long>();
        final ArrayList<Float> envelopeBalance_AL = new ArrayList<Float>();

        if (allEnvelopes.getCount() != 0) {
            while (allEnvelopes.moveToNext()){
                //String envelopeNameText = allEnvelopes.getString(1).toUpperCase() + " - Balance: " + allEnvelopes.getFloat(2);
                envelopeName_AL.add(allEnvelopes.getString(1).toUpperCase());
                envelopeID_AL.add(allEnvelopes.getLong(0));
                envelopeBalance_AL.add(allEnvelopes.getFloat(2));
            }
        }

        ArrayAdapter<String> envelopeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, envelopeName_AL);
        toEnvelopeSpinner.setAdapter(envelopeAdapter);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(envelopeTransfer.this, MainActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(envelopeTransfer.this, ViewEnvelopeDetails.class);
                intent.putExtra("EXTRA_SESSION_ID", passedEnvelopeID);
                startActivity(intent);
            }
        });

        toEnvelopeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEnvelopeID = envelopeID_AL.get(position);
                toEnvelopeBalance = envelopeBalance_AL.get(position);
                toEnvelopeName = envelopeName_AL.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Float.parseFloat(amount.getText().toString()) > fromEnvelopeBalance){
                    Toast.makeText(envelopeTransfer.this,"Error: Amount exceeds available amount",Toast.LENGTH_SHORT).show();
                }else {
                    fromEnvelopeBalance -= Float.parseFloat(amount.getText().toString());
                    toEnvelopeBalance += Float.parseFloat(amount.getText().toString());

                    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
                    String date = sdf.format(new Date());

                    String fromRegisterEntry = "Transfer: " + toEnvelopeName.toUpperCase();
                    String toRegisterEntry = "Transfer: " + fromEnvelopeName.toUpperCase();

                    db.insertRegisterData(passedEnvelopeID, date, fromRegisterEntry, -Float.parseFloat(amount.getText().toString()), 0.0f);
                    db.insertRegisterData(selectedEnvelopeID, date, toRegisterEntry, Float.parseFloat(amount.getText().toString()), 0.0f);

                    db.updateEnvelopeBalance(passedEnvelopeID, fromEnvelopeBalance);
                    db.updateEnvelopeBalance(selectedEnvelopeID, toEnvelopeBalance);
                    Intent intent = new Intent(envelopeTransfer.this, ViewEnvelopeDetails.class);
                    intent.putExtra("EXTRA_SESSION_ID", passedEnvelopeID);
                    startActivity(intent);
                }
            }
        });

    }
}
