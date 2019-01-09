package domeny.niema.klient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    ParseObject current_travel;
    private int mYear, mMonth, mDay;

    private Button show_reservations;
    private View.OnClickListener show_reservationsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            show_reservations_buttonClicked();
        }
    };
    private Button info_button;
    //Aleks DO TESTOW UTWORZONE METODY
    //logout button listener
    private Button logout_button;
    private View.OnClickListener logout_buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logoutButtonClicked();
        }
    };
    private Button view_all;

    //view button listener
    private LinearLayout ll;
    private View.OnClickListener info_buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            info_buttonClicked();
        }
    };
    private View.OnClickListener view_allOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view_all_buttonClicked();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        logout_button = findViewById(R.id.logout_button);
        view_all = findViewById(R.id.view_all);
        show_reservations = findViewById(R.id.show_reservations);
        info_button = findViewById(R.id.info);


        ll = findViewById(R.id.layoutinside);


        info_buttonClicked();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()


        );


//ustawianie listenerow

        logout_button.setOnClickListener(logout_buttonOnClickListener);
        view_all.setOnClickListener(view_allOnClickListener);
        show_reservations.setOnClickListener(show_reservationsOnClickListener);
        info_button.setOnClickListener(info_buttonOnClickListener);


    }

    private void info_buttonClicked() {
        ll.removeAllViews();


        TextView title = new TextView(getApplicationContext());
        title.setText("Hello Traveler!");
        title.setTypeface(null, Typeface.BOLD);
        title.setTextSize(18);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 100);
        params1.gravity = Gravity.CENTER;
        title.setLayoutParams(params1);
        ll.addView(title);


        TextView howtomodify = new TextView(getApplicationContext());
        howtomodify.setText("How to navigate:");
        howtomodify.setTypeface(null, Typeface.BOLD);
        howtomodify.setTextSize(15);
        ll.addView(howtomodify);

        TextView howtomodify2 = new TextView(getApplicationContext());
        howtomodify2.setText("Firstly click button - 'View Travels', next you should pick travel you want to see. Have fun!");
        ll.addView(howtomodify2);

    }



    private void logoutButtonClicked() {
        final ProgressDialog dlg = new ProgressDialog(UserActivity.this);
        dlg.setTitle("Please, wait a moment.");
        dlg.setMessage("Signing Out...");
        dlg.show();
        dlg.cancel();
        // logging out of Parse
        ParseUser.logOut();

        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);




    }

    private void show_reservations_buttonClicked() {
        if (CheckInternet.isOnline()) {
            ll.removeAllViews();


            ParseQuery<ParseObject> queryx = ParseQuery.getQuery("Reservations");
            queryx.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            queryx.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> Zarezerwowane, ParseException e) {
                    for (final ParseObject rezerwy : Zarezerwowane) {

                        final Button b = new Button(getApplicationContext());


                        b.setText(rezerwy.getString("travel_name") + "\n" + rezerwy.getDate("date") + "\n" + "Status: " + rezerwy.getString("status"));
                        b.setTextColor(Color.WHITE);


                        Drawable roundDrawable = getResources().getDrawable(R.drawable.rounded2);


                        b.setBackground(roundDrawable);


                        ll.addView(b);
                        TextView spacja0 = new TextView(getApplicationContext());
                        ll.addView(spacja0);

                        ParseQuery<ParseObject> queryxx = ParseQuery.getQuery("Trip");
                        queryxx.whereEqualTo("title", rezerwy.getString("travel_name"));
                        queryxx.getFirstInBackground(new GetCallback<ParseObject>() {
                            public void done(final ParseObject Travel, ParseException e) {
                                if (Travel == null) {
                                    Log.d("score", "The getFirst request failed.");
                                } else {
                                    b.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            ll.removeAllViews();

                                            current_travel = Travel;

                                            TextView title = new TextView(getApplicationContext());
                                            title.setText("Travel Title:");

                                            title.setTextSize(18);
                                            ll.addView(title);
                                            title.setTypeface(null, Typeface.BOLD);
                                            TextView title1 = new TextView(getApplicationContext());
                                            title1.setText(Travel.getString("title"));
                                            ll.addView(title1);


                                            TextView spacja = new TextView(getApplicationContext());
                                            ll.addView(spacja);
                                            TextView status1 = new TextView(getApplicationContext());
                                            status1.setText("Reservation status (queue/confirmed): ");
                                            status1.setTypeface(null, Typeface.BOLD);
                                            ll.addView(status1);
                                            TextView status = new TextView(getApplicationContext());
                                            status.setText(rezerwy.getString("status"));
                                            ll.addView(status);
                                            TextView spacja7 = new TextView(getApplicationContext());
                                            ll.addView(spacja7);
                                            TextView summary = new TextView(getApplicationContext());
                                            String TravelSummary = Travel.getString("summary");
                                            summary.setText("Travel Description:");
                                            summary.setTypeface(null, Typeface.BOLD);
                                            ll.addView(summary);
                                            TextView summary1 = new TextView(getApplicationContext());
                                            summary1.setText(TravelSummary);


                                            ll.addView(summary1);
                                            TextView spacja1 = new TextView(getApplicationContext());
                                            ll.addView(spacja1);

                                            TextView date = new TextView(getApplicationContext());
                                            Date data = Travel.getDate("date");


                                            SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");


                                            String str = form.format(data); // or if you want to save it in String str

                                            date.setText("Date: " + str);
                                            ll.addView(date);

                                            TextView spacja2 = new TextView(getApplicationContext());
                                            ll.addView(spacja2);


                                            TextView price = new TextView(getApplicationContext());
                                            Number TravelPrice = Travel.getNumber("price");
                                            String numberAsString = String.valueOf(TravelPrice);
                                            price.setText("Final price: " + numberAsString);
                                            price.setTypeface(null, Typeface.BOLD);
                                            ll.addView(price);

                                            TextView spacja3 = new TextView(getApplicationContext());
                                            ll.addView(spacja3);
                                            ImageView image = new ImageView(UserActivity.this);
                                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 720);

                                            params1.gravity = Gravity.CENTER;
                                            image.setLayoutParams(params1);


                                            ParseFile imagex = Travel.getParseFile("imagex");
                                            String imageUrl = imagex.getUrl();//live url
                                            Uri imageUri = Uri.parse(imageUrl);
                                            ll.addView(image);
                                            Picasso.with(UserActivity.this).load(imageUri.toString()).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(image);

                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(UserActivity.this, "No internet connection, try again", Toast.LENGTH_LONG).show();

        }
    }





    private void view_all_buttonClicked() {

        if (CheckInternet.isOnline()) {
        ll.removeAllViews();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> TravelsList, ParseException e) {
                if (e == null) {
                    //sukces
                    int i = 0;
                    //zdefiniuj widok linearny w naszym scrollview
                    for (final ParseObject Travel : TravelsList) {

                        Button b = new Button(getApplicationContext());

                        final String TravelTitle = Travel.getString("title");
                        b.setText(TravelTitle);
                        b.setTextColor(Color.WHITE);


                        Drawable roundDrawable = getResources().getDrawable(R.drawable.rounded2);



                            b.setBackground(roundDrawable);



                        ll.addView(b);
                        TextView spacja0 = new TextView(getApplicationContext());
                        ll.addView(spacja0);
                        b.setId(i);
                        i++;

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                ll.removeAllViews();

                                current_travel = Travel;

                                TextView title = new TextView(getApplicationContext());
                                title.setText("Travel Title:");
                                System.out.println("TRAVEL ID:  " + Travel.getObjectId());
                                title.setTextSize(18);
                                ll.addView(title);
                                title.setTypeface(null, Typeface.BOLD);
                                TextView title1 = new TextView(getApplicationContext());
                                title1.setText(TravelTitle);
                                ll.addView(title1);


                                TextView spacja = new TextView(getApplicationContext());
                                ll.addView(spacja);
                                TextView summary = new TextView(getApplicationContext());
                                String TravelSummary = Travel.getString("summary");
                                summary.setText("Travel Description:");
                                summary.setTypeface(null, Typeface.BOLD);
                                ll.addView(summary);
                                TextView summary1 = new TextView(getApplicationContext());
                                summary1.setText(TravelSummary);


                                ll.addView(summary1);
                                TextView spacja1 = new TextView(getApplicationContext());
                                ll.addView(spacja1);

                                TextView date = new TextView(getApplicationContext());
                                Date data = Travel.getDate("date");


                                SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");


                                String str = form.format(data); // or if you want to save it in String str

                                date.setText("Date: " + str);
                                ll.addView(date);

                                TextView spacja2 = new TextView(getApplicationContext());
                                ll.addView(spacja2);


                                TextView price = new TextView(getApplicationContext());
                                Number TravelPrice = Travel.getNumber("price");
                                String numberAsString = String.valueOf(TravelPrice);
                                price.setText("Final price: " + numberAsString);
                                price.setTypeface(null, Typeface.BOLD);
                                ll.addView(price);

                                TextView spacja3 = new TextView(getApplicationContext());
                                ll.addView(spacja3);
                                ImageView image = new ImageView(UserActivity.this);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 720);

                                params1.gravity = Gravity.CENTER;
                                image.setLayoutParams(params1);


                                ParseFile imagex = Travel.getParseFile("imagex");
                                String imageUrl = imagex.getUrl();//live url
                                Uri imageUri = Uri.parse(imageUrl);
                                ll.addView(image);
                                Picasso.with(UserActivity.this).load(imageUri.toString()).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(image);


                                final Button reserve = new Button(getApplicationContext());
                                reserve.setText("Buy Travel");
                                reserve.setTypeface(null, Typeface.BOLD);
                                reserve.setTextColor(Color.WHITE);
                                Drawable roundDrawable = getResources().getDrawable(R.drawable.rounded2);
                                reserve.setBackground(roundDrawable);
                                reserve.setVisibility(View.VISIBLE);
                                ll.addView(reserve);

                                ParseQuery<ParseObject> queryx = ParseQuery.getQuery("Reservations");
                                queryx.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                                queryx.findInBackground(new FindCallback<ParseObject>() {

                                    @Override
                                    public void done(List<ParseObject> Zarezerwowane, ParseException e) {
                                        for (ParseObject rezerwy : Zarezerwowane) {


                                            if (rezerwy.getString("username").equals(ParseUser.getCurrentUser().getUsername()) && rezerwy.getString("travel_id").equals(Travel.getObjectId())) {
                                                reserve.setVisibility(View.INVISIBLE);
                                            }
                                        }

                                        reserve.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this)
                                                        .setTitle("Send " + Travel.getNumber("price") + "$ to")
                                                        .setMessage("Bank account: 123123123\nPayment title: " + Travel.getString("title") + " " + ParseUser.getCurrentUser())
                                                        .setPositiveButton("I'm buying", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                ParseObject rezerwacja = new ParseObject("Reservations");
                                                                rezerwacja.put("username", ParseUser.getCurrentUser().getUsername());
                                                                rezerwacja.put("travel_id", Travel.getObjectId());
                                                                rezerwacja.put("travel_name", Travel.getString("title"));
                                                                rezerwacja.put("price", Travel.getNumber("price"));
                                                                rezerwacja.put("date", Travel.getDate("date"));
                                                                rezerwacja.put("status", "queue");

                                                                final ProgressDialog dlg = new ProgressDialog(UserActivity.this);
                                                                dlg.setTitle("Please, wait a moment.");
                                                                dlg.setMessage("Uploading...");
                                                                dlg.show();
                                                                rezerwacja.saveInBackground(new SaveCallback() {
                                                                    public void done(ParseException e) {
                                                                        if (e == null) {

                                                                            dlg.dismiss();
                                                                            view_all_buttonClicked();
                                                                        } else {

                                                                            dlg.dismiss();

                                                                            view_all_buttonClicked();
                                                                        }
                                                                    }
                                                                });


                                                            }


                                                        });


                                                AlertDialog ok = builder.create();
                                                ok.show();
                                            }
                                        });
                                    }
                                });






                            }
                        });


                    }


                    Log.d("score", "Retrieved " + TravelsList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


        } else {
            Toast.makeText(UserActivity.this, "No internet connection, try again", Toast.LENGTH_LONG).show();
        }


    }
}






