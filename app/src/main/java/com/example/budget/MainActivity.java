package com.example.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button viewEnvelopes = findViewById(R.id.mainMenuViewEnvelopesButton);
        final Button accountsButton = findViewById(R.id.accountsButton);
        final Button quickAdd = findViewById(R.id.mainMenuQuickAddButton);
        accountsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListAccountsActivity.class));
            }
        });
        viewEnvelopes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewEnvelopes.class));
            }
        });
        quickAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QuickAddActivity.class));
            }
        });
    }
}
