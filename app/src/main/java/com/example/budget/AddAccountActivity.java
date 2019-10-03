package com.example.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddAccountActivity extends AppCompatActivity {

    EditText account_name, account_balance, account_credit_limit;
    String accountName;
    Float accountBalance, accountCreditLimit;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        Button mainMenu = findViewById(R.id.addAccountMainMenuButton);
        Button backButton = findViewById(R.id.addAccountBackButton);
        Button submitButton = findViewById(R.id.addAccountSubmitButton);

        account_name = findViewById(R.id.addAccountNameET);
        account_balance = findViewById(R.id.addAccountBalanceET);
        account_credit_limit = findViewById(R.id.addAccountCreditLimitET);

        db = new DatabaseHelper(this);
        final LinearLayout creditLimitLL = findViewById(R.id.addAccountCreditLimitLL);
        final CheckBox creditCardCB = findViewById(R.id.addAccountIsCreditCardCheckBox);
        creditCardCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(creditCardCB.isChecked()){
                    creditLimitLL.setVisibility(View.VISIBLE);
                } else {
                    creditLimitLL.setVisibility(View.INVISIBLE);
                    account_credit_limit.setText("0");
                }
            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddAccountActivity.this, MainActivity.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddAccountActivity.this, ListAccountsActivity.class));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!creditCardCB.isChecked()) {
                    account_credit_limit.setText("0");
                }
                if (account_name.getText().toString().isEmpty() | account_balance.getText().toString().isEmpty() | account_credit_limit.getText().toString().isEmpty()){
                    Toast.makeText(AddAccountActivity.this,"ERROR, FIELDS CAN NOT BE EMPTY", Toast.LENGTH_LONG).show();
                }else {


                    accountName = account_name.getText().toString();
                    accountBalance = Float.parseFloat(account_balance.getText().toString());
                    accountCreditLimit = Float.parseFloat(account_credit_limit.getText().toString());
                    Boolean isCC = creditCardCB.isChecked();
                    String creditCard;
                    if (isCC) {
                        creditCard = "true";
                    } else {
                        creditCard = "false";
                    }
                    Float availableCredit;
                    if (accountCreditLimit==0){
                        availableCredit = 0.0f;
                    }else {
                        availableCredit = accountCreditLimit - accountBalance;
                    }

                    db.insertAccountData(accountName, accountBalance, availableCredit, accountCreditLimit, creditCard);
                    startActivity(new Intent(AddAccountActivity.this, ListAccountsActivity.class));
                }
            }
        });
    }
}
