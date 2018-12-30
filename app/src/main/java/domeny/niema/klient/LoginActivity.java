package domeny.niema.klient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameView;
    private EditText passwordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()

        );


        usernameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);

        final Button login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating the log in data
                boolean validationError = false;

                StringBuilder validationErrorMessage = new StringBuilder("Please, insert ");
                if (isEmpty(usernameView)) {
                    validationError = true;
                    validationErrorMessage.append("an username");
                }
                if (isEmpty(passwordView)) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("a password");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }


                String login = usernameView.getText().toString();
                String haslo = passwordView.getText().toString();
                loginbuttonClicked(login, haslo);


            }





        });
        final Button guest_enter_button = findViewById(R.id.guest_enter);

        guest_enter_button.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, TravelsForGuests.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        final TextView signup_button = findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private boolean isEmpty(EditText text) {
        return text.getText().toString().trim().length() <= 0;
    }

    public void alertDisplayer(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (usernameView.getText().toString().equals("admin")) {
                            Intent intent = new Intent(LoginActivity.this, AdminPanel.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);


                        } else {
                            Intent intent = new Intent(LoginActivity.this, TravelsForUsers.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    protected boolean loginbuttonClicked(final String login, final String haslo) {
        final boolean[] sukces = new boolean[1];
        //Setting up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(LoginActivity.this);
        dlg.setTitle("Please, wait a moment.");
        dlg.setMessage("Logging in...");
        dlg.show();

        ParseUser.logInInBackground(usernameView.getText().toString(), passwordView.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    sukces[0] = true;
                    dlg.dismiss();

                    //JESLI JEST ODPOWIEDZ OD SERWERA -> stworz okienko ze udalo sie zalogowac

                    alertDisplayer("Succesful login", "Welcome back" + usernameView.getText().toString() + "!");

                } else {
                    sukces[0] = false;
                    dlg.dismiss();
                    ParseUser.logOut();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return sukces[0];
    }


}