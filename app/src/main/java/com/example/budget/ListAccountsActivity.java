package com.example.budget;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListAccountsActivity extends AppCompatActivity {

    public LinearLayout accountsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_accounts);

        accountsLayout = findViewById(R.id.accountsAccountsLL);
        TextView titleBar = findViewById(R.id.accountsTitleBarTV);


        Button mainMenu = findViewById(R.id.accountsMainMenuButton);
        Button addAccount = findViewById(R.id.accountsAddAccountButton);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListAccountsActivity.this, MainActivity.class));
            }
        });

        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListAccountsActivity.this, AddAccountActivity.class));
            }
        });

        updateUI();
    }

    private void updateUI(){

        accountsLayout.removeAllViews();

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor accounts = db.getAllAccounts();
        Float totalCash = 0.0f;

        String totalAssetsS = "Total assets: $0.00";

        TextView totalAssets = findViewById(R.id.accountsTotalAssetTV);
        totalAssets.setText(totalAssetsS);

        if (accounts.getCount() != 0){
            while (accounts.moveToNext()){
                final int idNum = accounts.getInt(0);
                final String name = accounts.getString(1);
                final Float balance = accounts.getFloat(2);
                final Float available_credit = accounts.getFloat(3);
                final Float credit_limit = accounts.getFloat(4);
                final String isCC = accounts.getString(5);


                Button newAccount = new Button(this);
                String buttonText = null;
                if (!isCC.contains("true")){
                    buttonText = name + " - " + String.format("$%.2f", balance);
                    totalCash += balance;
                } else {
                    buttonText = name + " - " + String.format("$%.2f", available_credit);
                    totalCash += available_credit;
                }

                totalAssetsS = "Total assets: " + String.format("$%.2f", totalCash);

                totalAssets = findViewById(R.id.accountsTotalAssetTV);
                totalAssets.setText(totalAssetsS);

                newAccount.setText(buttonText);
                newAccount.setTag(idNum);
                newAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ListAccountsActivity.this, AccountDetailView.class);
                        intent.putExtra("ID", Integer.valueOf(v.getTag().toString()));
                        startActivity(intent);
                    }
                });
                accountsLayout.addView(newAccount);
            }
        }
    }
}
