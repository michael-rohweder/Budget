package com.example.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AccountEditView extends AppCompatActivity {

    EditText account_name, account_balance, account_credit_limit;
    String accountName;
    Float accountBalance, accountCreditLimit;
    DatabaseHelper db;

    public int passedID;
    public String name, isCC;
    public Float balance, limit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            passedID = extras.getInt("ID");
            name = extras.getString("name");
            balance=extras.getFloat("balance");
            limit=extras.getFloat("credit_limit");
            isCC=extras.getString("isCC");
        }

        Button mainMenu = findViewById(R.id.editViewAccountMainMenuButton);
        Button backButton = findViewById(R.id.editViewAccountBackButton);
        Button submitButton = findViewById(R.id.editViewAccountSubmitButton);

        account_name = findViewById(R.id.editViewAccountNameET);
        account_balance = findViewById(R.id.editViewAccountBalanceET);
        account_credit_limit = findViewById(R.id.editViewAccountCreditLimitET);
        TextView tv4 = findViewById(R.id.editViewAccountCreditLimitTV);
        account_name.setText(name);
        account_balance.setText(balance.toString());
        if (isCC.contains("false")){
            account_credit_limit.setVisibility(View.INVISIBLE);
            tv4.setVisibility(View.INVISIBLE);
        }
        account_credit_limit.setText(limit.toString());


        db = new DatabaseHelper(this);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountEditView.this, MainActivity.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountEditView.this, ListAccountsActivity.class));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account_name.getText().toString().isEmpty() | account_balance.getText().toString().isEmpty() | account_credit_limit.getText().toString().isEmpty()){
                    Toast.makeText(AccountEditView.this,"ERROR, FIELDS CAN NOT BE EMPTY", Toast.LENGTH_LONG).show();
                }else {


                    accountName = account_name.getText().toString();
                    accountBalance = Float.parseFloat(account_balance.getText().toString());
                    accountCreditLimit = Float.parseFloat(account_credit_limit.getText().toString());
                    Float availableCredit;
                    if (accountCreditLimit==0){
                        availableCredit = 0.0f;
                    }else {
                        availableCredit = accountCreditLimit - accountBalance;
                    }

                    db.updateAccountData(passedID, accountName, accountBalance, availableCredit, accountCreditLimit);
                    startActivity(new Intent(AccountEditView.this, ListAccountsActivity.class));
                }
            }
        });
    }
}

