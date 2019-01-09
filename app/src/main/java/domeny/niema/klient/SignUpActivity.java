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

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
public class SignUpActivity extends AppCompatActivity {
    private EditText usernameView;
    private EditText passwordView;
    private EditText passwordAgainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()



        );
        final TextView back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dlg = new ProgressDialog(SignUpActivity.this);
                dlg.setTitle("Please, wait a moment.");
                dlg.setMessage("Returning to the login section...");
                dlg.show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                dlg.dismiss();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        usernameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);
        passwordAgainView = findViewById(R.id.passwordAgain);




        final Button signup_button = findViewById(R.id.signup_button);

        if (CheckInternet.isOnline()) {
            signup_button.setOnClickListener(new View.OnClickListener() {
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
                    if (isEmpty(passwordAgainView)) {
                        if (validationError) {
                            validationErrorMessage.append(" and ");
                        }
                        validationError = true;
                        validationErrorMessage.append("your password again");
                    } else {
                        if (!isMatching(passwordView, passwordAgainView)) {
                            if (validationError) {
                                validationErrorMessage.append(" and ");
                            }
                            validationError = true;
                            validationErrorMessage.append("the same password twice.");
                        }
                    }
                    validationErrorMessage.append(".");

                    if (validationError) {
                        Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    //Setting up a progress dialog
                    final ProgressDialog dlg = new ProgressDialog(SignUpActivity.this);
                    dlg.setTitle("Please, wait a moment.");
                    dlg.setMessage("Signing up...");
                    dlg.show();

                    ParseUser user = new ParseUser();
                    user.setUsername(usernameView.getText().toString());
                    user.setPassword(passwordView.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                dlg.dismiss();
                                Intent intent = new Intent(SignUpActivity.this, UserActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } else {
                                dlg.dismiss();
                                ParseUser.logOut();
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            });
        } else {
            Toast.makeText(SignUpActivity.this, "No internet connection, trying again", Toast.LENGTH_LONG).show();

        }
    }

    private boolean isEmpty(EditText text) {
        return text.getText().toString().trim().length() <= 0;
    }

    private boolean isMatching(EditText text1, EditText text2){
        return text1.getText().toString().equals(text2.getText().toString());
    }


}