package com.team404.trackmyday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by James on 11/14/2016.
 */

public class ContactInfo extends AppCompatActivity implements View.OnClickListener {

    private Button btnAdd;
    private EmergencyCoordinator coordinator;
    private TextView txtCName, txtCPhone;
    private EditText contactName, contactPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_info);

        coordinator = new EmergencyCoordinator();

        txtCName = (TextView) findViewById(R.id.txtCName);
        txtCPhone = (TextView) findViewById(R.id.txtCPhone);
        contactName = (EditText) findViewById(R.id.contactName);
        contactPhone = (EditText) findViewById(R.id.contactPhone);

        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btnAdd:
                coordinator.setContactName(contactName.getText().toString());
                coordinator.setContactNumber(contactPhone.getText().toString());
                break;
        }
    }
}
