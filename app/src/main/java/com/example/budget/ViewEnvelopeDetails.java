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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.StrictMath.abs;

public class ViewEnvelopeDetails extends AppCompatActivity {

    public static DatabaseHelper db;
    public Long value;
    public Float balance;
    public TextView titleBar;
    public String name;
    public LinearLayout holder;
    public Float fAmount;

    public String date;
    public String payee;
    public Float amount;
    public int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_envelope_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getLong("EXTRA_SESSION_ID");
        }

        name = null;
        balance = 0.0f;
        titleBar = findViewById(R.id.viewEnvelopeDetailsTitleBarTV);

        Button transfer = findViewById(R.id.viewEnvelopeDetailsTransferButton);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEnvelopeDetails.this, envelopeTransfer.class);
                intent.putExtra("envelopeID", value);
                startActivity(intent);
            }
        });

        db = new DatabaseHelper(this);
        final DatabaseHelper registerDB = new DatabaseHelper(this);

        holder =  findViewById(R.id.viewEnvelopeDetailsDetailLL);

        Cursor envRes = db.getEnvelopeData(value);

        if (envRes.getCount() != 0){
            while (envRes.moveToNext()){
                name = envRes.getString(1);
                balance = envRes.getFloat(2);
            }
        }
        titleBar.setText(name + " - " + String.format("$%.2f", balance));
        titleBar.setTag(value);
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.getContext());
                builder.setTitle("Edit Envelope");
                final EditText nameBox = new EditText(holder.getContext());
                final EditText amountBox = new EditText(holder.getContext());

                nameBox.setHint("Name");
                nameBox.setText(name);
                amountBox.setHint("Amount");
                amountBox.setText(balance.toString());

                final Long idNum = Long.parseLong(v.getTag().toString());

                amountBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                LinearLayout lay = new LinearLayout(holder.getContext());
                lay.setOrientation(LinearLayout.VERTICAL);
                lay.addView(nameBox);
                lay.addView(amountBox);

                builder.setView(lay);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fAmount = Float.parseFloat(amountBox.getText().toString());
                        balance = Float.parseFloat(amountBox.getText().toString());

                        name=nameBox.getText().toString();

                        int res = db.updateEnvelopeData(idNum, nameBox.getText().toString(), balance);
                        Cursor res2 = db.updateRegisterStartingData(idNum, balance);

                        if (res != -1){}else{}
                        if (res2.getCount()!=0){}else{}
                        holder.removeAllViews();
                        displayUI();
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

        Button add = findViewById(R.id.viewEnvelopeDetailsAddButton);
        ImageButton delete = findViewById(R.id.viewEnvelopeDetailsDeleteButton);
        Button mainMenu = findViewById(R.id.viewEnvelopeDetailsMainMenuButton);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewEnvelopeDetails.this, MainActivity.class));
            }
        });
        Button back = findViewById(R.id.viewEnvelopeDetailsBackButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEnvelopeDetails.this, ViewEnvelopes.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteEnvelopeData(value);
                Button deleted = ViewEnvelopes.layoutHolder.findViewWithTag(value);
                ViewEnvelopes.layoutHolder.removeView(deleted);
                Intent intent = new Intent(ViewEnvelopeDetails.this, ViewEnvelopes.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.getContext());
                builder.setTitle("New Transaction");
                final EditText dateBox = new EditText(holder.getContext());
                final EditText payeeBox = new EditText(holder.getContext());
                final EditText amountBox = new EditText(holder.getContext());

                dateBox.setHint("Date");

                SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
                String currentDate = sdf.format(new Date());

                dateBox.setText(currentDate);
                payeeBox.setHint("Payee");
                payeeBox.setFocusedByDefault(true);
                amountBox.setHint("Amount");

                final RadioGroup rg = new RadioGroup(holder.getContext());
                rg.setOrientation(RadioGroup.HORIZONTAL);
                RadioButton deposit = new RadioButton(holder.getContext());
                RadioButton debit = new RadioButton(holder.getContext());
                deposit.setId(R.id.Deposit);
                debit.setId(R.id.Debit);
                deposit.setText("Credit");
                debit.setText("Debit");
                debit.setChecked(true);
                rg.addView(debit);
                rg.addView(deposit);
                amountBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                LinearLayout lay = new LinearLayout(holder.getContext());
                lay.setOrientation(LinearLayout.VERTICAL);
                lay.addView(dateBox);
                lay.addView(payeeBox);
                lay.addView(amountBox);
                lay.addView(rg);

                builder.setView(lay);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int checkedRadio = rg.getCheckedRadioButtonId();
                        fAmount = Float.parseFloat(amountBox.getText().toString());
                        if (checkedRadio == R.id.Debit){
                            fAmount = -fAmount;
                        }
                        long result = registerDB.insertRegisterData(value,dateBox.getText().toString(),payeeBox.getText().toString().toUpperCase(),fAmount, fAmount);
                        if (result != -1){
                            holder.removeAllViews();
                            displayUI();
                        } else {
                            Toast.makeText(ViewEnvelopeDetails.this,"ERROR ON INSERTION",Toast.LENGTH_SHORT ).show();
                        }

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
        displayUI();
      }
    public void displayUI(){
        String name=null;
        Cursor res = db.getRegisterData(value);
        Cursor unsortedRes = db.GetRegisterDataUnSorted(value);
        Cursor envelopeDB = db.getEnvelopeData(value);
        while (envelopeDB.moveToNext()){
            name = envelopeDB.getString(1);
        }
        balance = 0.0f;

        if (unsortedRes.getCount() != 0){
            while (unsortedRes.moveToNext()){
                int id = unsortedRes.getInt(0);
                Long envID = unsortedRes.getLong(1);
                date = unsortedRes.getString(2);
                payee = unsortedRes.getString(3);
                amount = unsortedRes.getFloat(4);
                if (unsortedRes.getString(3).contains("STARTING")){
                    balance = amount;
                }else {
                    balance += unsortedRes.getFloat(4);

                }

                Cursor update = db.updateRegisterBalance(id, balance);
                if (update.getCount()==0){

                }else{

                }
            }
            db.updateEnvelopeData(value, name, balance);
            String titleBarTV = name + " - " + String.format("$%.2f", balance);
            titleBar.setText(titleBarTV);
        }
        if (res.getCount() != 0) {

            while (res.moveToNext()){
                ID = res.getInt(0);
                final Long envID = res.getLong(1);
                date = res.getString(2);
                payee = res.getString(3);
                amount = res.getFloat(4);
                balance = res.getFloat(5);

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = 1;

                LinearLayout row = new LinearLayout(holder.getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView dateTV = new TextView(holder.getContext());
                TextView payeeTV = new TextView(holder.getContext());
                TextView amountTV = new TextView(holder.getContext());
                TextView balanceTV = new TextView(holder.getContext());

                dateTV.setText(date);
                payeeTV.setText(payee);

                String amt = String.format("$%.2f", amount);
                String bal = String.format("$%.2f", balance);
                amountTV.setText(amt);
                balanceTV.setText(bal);

                if (amount < 0) {
                    amountTV.setTextColor(Color.parseColor("#FF0000"));
                }
                if (balance < 0){
                    balanceTV.setTextColor(Color.parseColor("#FF0000"));
                }

                dateTV.setLayoutParams(p);
                payeeTV.setLayoutParams(p);
                amountTV.setLayoutParams(p);
                balanceTV.setLayoutParams(p);


                row.addView(dateTV);
                row.addView(payeeTV);
                row.addView(amountTV);
                row.addView(balanceTV);
                row.setTag(ID);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                ID = Integer.valueOf(v.getTag().toString());
                                int registerID = Integer.valueOf(v.getTag().toString());
                                Cursor rows = db.getRegisterData(registerID);

                                if (rows.getCount() != 0) {
                                    while (rows.moveToNext()) {

                                        date = rows.getString(2);
                                        payee = rows.getString(3);
                                        amount = rows.getFloat(4);
                                        System.out.println("**************************************************************************************");
                                        System.out.println("***************************  count: " + rows.getCount());
                                        System.out.println("***************************  id: " + ID);
                                        System.out.println("***************************  date: " + date);
                                        System.out.println("***************************  payee: " + payee);
                                        System.out.println("***************************  amount: " + amount);
                                        System.out.println("**************************************************************************************");
                                    }

                                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.getContext());
                                    builder.setTitle("Edit Transaction");
                                    final EditText dateBox = new EditText(holder.getContext());
                                    final EditText PayeeBox = new EditText(holder.getContext());
                                    final EditText amountBox = new EditText(holder.getContext());

                                    dateBox.setHint("Date");

                                    dateBox.setText(date);

                                    PayeeBox.setHint("Payee");

                                    PayeeBox.setFocusedByDefault(true);

                                    amountBox.setHint("Amount");
                                    Float amountInBox = abs(amount);


                                    PayeeBox.setText(payee);

                                    amountBox.setText(amountInBox.toString());

                                    final RadioGroup rg = new RadioGroup(holder.getContext());
                                    rg.setOrientation(RadioGroup.HORIZONTAL);
                                    RadioButton deposit = new RadioButton(holder.getContext());
                                    RadioButton debit = new RadioButton(holder.getContext());
                                    deposit.setId(R.id.Deposit);
                                    debit.setId(R.id.Debit);
                                    deposit.setText("Credit");
                                    debit.setText("Debit");
                                    debit.setChecked(true);
                                    rg.addView(debit);
                                    rg.addView(deposit);
                                    amountBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                    LinearLayout lay = new LinearLayout(holder.getContext());
                                    lay.setOrientation(LinearLayout.VERTICAL);
                                    lay.addView(dateBox);
                                    lay.addView(PayeeBox);
                                    lay.addView(amountBox);
                                    lay.addView(rg);

                                    builder.setView(lay);

                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                                int checkedRadio = rg.getCheckedRadioButtonId();

                                                fAmount = Float.parseFloat(amountBox.getText().toString());
                                                if (checkedRadio == R.id.Debit) {
                                                    fAmount = -fAmount;
                                                }
                                                if (payee.equals("STARTING")){
                                                    PayeeBox.setText("STARTING");
                                                }
                                                Cursor update = db.updateRegisterData(ID, dateBox.getText().toString(), PayeeBox.getText().toString().toUpperCase(), fAmount, fAmount);
                                                if (update.getCount() != 0) {
                                                } else {
                                                }
                                                holder.removeAllViews();
                                                displayUI();
                                        }
                                    });
                                    builder.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!payee.contains("STARTING")) {
                                                int result = db.deleteRegisterData(ID);
                                                holder.removeAllViews();
                                                displayUI();
                                            } else {
                                                Toast.makeText(holder.getContext(), "CAN NOT DELETE STARTING TRANSACTION", Toast.LENGTH_LONG).show();
                                            }
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
                        }
                    });
                holder.addView(row);
            }
        }
    }
}
