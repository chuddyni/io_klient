package domeny.niema.klient;

import android.app.ProgressDialog;
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


                if (CheckInternet.isOnline()) {

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
                    AuthenticateAndAuthorize(login, haslo);


                } else {
                    Toast.makeText(LoginActivity.this, "No internet connection, trying again", Toast.LENGTH_LONG).show();

                }


            }

        });
        final Button guest_enter_button = findViewById(R.id.guest_enter);

        guest_enter_button.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GuestActivity.class);
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


    private void AuthenticateAndAuthorize(final String login, final String haslo) {

        //Setting up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(LoginActivity.this);
        dlg.setTitle("Please, wait a moment.");
        dlg.setMessage("Logging in...");
        dlg.show();

        //authenticate

        ParseUser.logInInBackground(login, haslo, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {

                    dlg.dismiss();

                    //authorize
                    if (login.equals("admin")) {
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    } else {
                        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    //JESLI JEST ODPOWIEDZ OD SERWERA -> stworz okienko ze udalo sie zalogowac


                } else {

                    dlg.dismiss();
                    ParseUser.logOut();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }


}