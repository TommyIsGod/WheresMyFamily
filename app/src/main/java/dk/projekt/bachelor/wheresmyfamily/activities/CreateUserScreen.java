package dk.projekt.bachelor.wheresmyfamily.activities;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableJsonOperationCallback;

import dk.projekt.bachelor.wheresmyfamily.R;
import dk.projekt.bachelor.wheresmyfamily.helper.BaseActivity;

public class CreateUserScreen extends BaseActivity {

    private final String TAG = "CreateUserScreen";
    private Button btnRegister;
    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private EditText mTxtConfirm;
    private EditText mTxtEmail;
    private EditText mTxtPhone;
    private Activity mActivity;
    private CheckBox mChbChild;
    private boolean chcBoxChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_screen);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mActivity = this;

        //Get UI elements
        btnRegister = (Button) findViewById(R.id.btnRegister);
        mTxtUsername = (EditText) findViewById(R.id.txtRegisterUsername);
        mTxtPassword = (EditText) findViewById(R.id.txtRegisterPassword);
        mTxtConfirm = (EditText) findViewById(R.id.txtRegisterConfirm);
        mTxtEmail = (EditText) findViewById(R.id.txtRegisterEmail);
        mTxtPhone = (EditText) findViewById(R.id.txtRegisterPhone);
        mChbChild = (CheckBox) findViewById(R.id.checkBoxRegChild);

        //Set click listeners
        btnRegister.setOnClickListener(registerClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBoxRegChild:
                if (checked)
                {
                //Child
                chcBoxChild = true;
                Toast.makeText(getApplicationContext(),
                        "Du er valgt som barn", Toast.LENGTH_SHORT).show();
                }
                else
                //Parent
                chcBoxChild = false;
                break;
        }
    }

    View.OnClickListener registerClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mTxtUsername.getText().toString().equals("") ||
                    mTxtPassword.getText().toString().equals("") ||
                    mTxtConfirm.getText().toString().equals("") ||
                    mTxtEmail.getText().toString().equals("") ||
                    mTxtPhone.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Alle felter skal udfyldes for at registrere", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "You must enter all fields to register");
                return;
            } else if (!mTxtPassword.getText().toString().equals(mTxtConfirm.getText().toString())) {
                Toast.makeText(getApplicationContext(),
                        "Kodeordene er ikke ens", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "The passwords you've entered don't match");
                return;
            } else {
                mMobileServicesClient.registerUser(mTxtUsername.getText().toString(),
                        mTxtPassword.getText().toString(),
                        mTxtConfirm.getText().toString(),
                        mTxtEmail.getText().toString(),
                       mTxtPhone.getText().toString(), chcBoxChild,
                        new TableJsonOperationCallback() {
                            @Override
                            public void onCompleted(JsonObject jsonObject, Exception exception,
                                                    ServiceFilterResponse response) {
                                if (exception == null) {

                                    mMobileServicesClient.setUserAndSaveData(jsonObject);

                                    mActivity.finish();
                                    Toast.makeText(getApplicationContext(),
                                            "Din Brugerprofil er nu oprettet", Toast.LENGTH_SHORT).show();

                                    if (chcBoxChild == true) {
                                        Intent loggedInChildIntent = new Intent(getApplicationContext(), LoggedInChild.class);
                                        startActivity(loggedInChildIntent);
                                    }
                                    else {
                                        Intent loggedInIntent = new Intent(getApplicationContext(), LoggedInParent.class);
                                        startActivity(loggedInIntent);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Der skete en fejl under registringen af bruger: "+ exception.getCause().getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "There was an error registering the user: " + exception.getMessage());
                                }
                            }
                        });
            }
        }
    };
}
