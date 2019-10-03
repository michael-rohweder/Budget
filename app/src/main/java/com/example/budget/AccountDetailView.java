package com.example.budget;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AccountDetailView extends AppCompatActivity {

    public int passedID;
    public DatabaseHelper db;

    public String accountName;
    public Float accountBalance, accountCreditLimit, accountCreditAvailable;
    public String isCC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            passedID = extras.getInt("ID");
        }

        /*Button addTransaction = findViewById(R.id.addTransactionButton);
        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountDetailView.this,"FEATURE COMING SOON", Toast.LENGTH_LONG).show();
            }
        });*/



        ImageButton delete =findViewById(R.id.viewAccountDeleteButton);

        TextView titleBar = findViewById(R.id.viewAccountTitleBarTV);


        TextView balanceTV = findViewById(R.id.viewAccountBalanceTV);

        TextView creditAvailableTV = findViewById(R.id.viewAccountCreditAvailableTV);
        TextView creditLimitTV = findViewById(R.id.viewAccountCreditLimitTV);

        TextView showCreditLimitTV = findViewById(R.id.viewAccountShowCreditLimitTV);
        TextView showCreditAvailableTV = findViewById(R.id.viewAccountShowCreditAvailableTV);
        TextView showBalanceTV = findViewById(R.id.viewAccountShowBalanceTV);

        Button backButton = findViewById(R.id.viewAccountBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountDetailView.this, ListAccountsActivity.class));
            }
        });

        Button mainMenu = findViewById(R.id.viewAccountMainMenuButton);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountDetailView.this, MainActivity.class));
            }
        });

        db = new DatabaseHelper(this);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteAccountData(passedID);
                startActivity(new Intent(AccountDetailView.this, ListAccountsActivity.class));
            }
        });
        Cursor accountData = db.getSpecificAccount(passedID);
        if (accountData.getCount() != 0) {
            while (accountData.moveToNext()) {
                accountName = accountData.getString(1);
                accountBalance = accountData.getFloat(2);
                accountCreditAvailable = accountData.getFloat(3);
                accountCreditLimit = accountData.getFloat(4);
                isCC = accountData.getString(5);
            }
        }
        titleBar.setText(accountName);
        showBalanceTV.setText(String.format("$%.2f",accountBalance));
        showCreditAvailableTV.setText(String.format("$%.2f",accountCreditAvailable));
        showCreditLimitTV.setText(String.format("$%.2f",accountCreditLimit));

        if (!isCC.contains("true")) {
            creditAvailableTV.setVisibility(View.INVISIBLE);
            creditLimitTV.setVisibility(View.INVISIBLE);

            showCreditAvailableTV.setVisibility(View.INVISIBLE);
            showCreditLimitTV.setVisibility(View.INVISIBLE);
        }

        View.OnClickListener header = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetailView.this, AccountEditView.class);
                intent.putExtra("ID", passedID);
                intent.putExtra("name", accountName);
                intent.putExtra("balance", accountBalance);
                intent.putExtra("credit_limit", accountCreditLimit);
                intent.putExtra("isCC", isCC);
                startActivity(intent);
            }
        };

        balanceTV.setOnClickListener(header);
        creditAvailableTV.setOnClickListener(header);
        creditLimitTV.setOnClickListener(header);
        titleBar.setOnClickListener(header);

        updateUI();
    }

    private void updateUI(){
        //**************************************
        //GET ACCOUNT REGISTER DATA
        //**************************************

        //**************************************
        //DISPLAY ACCOUNT REGISTER DATA
        //**************************************
    }
}
