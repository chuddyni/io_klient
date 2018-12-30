package domeny.niema.klient;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TravelsForGuests extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    ParseObject current_travel;
    private int mYear, mMonth, mDay;

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
        setContentView(R.layout.activity_travels_for_guests);
        logout_button = findViewById(R.id.logout_button);
        view_all = findViewById(R.id.view_all);

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


    private void alertDisplayer(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TravelsForGuests.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        Intent intent = new Intent(TravelsForGuests.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }


                });


        AlertDialog ok = builder.create();
        ok.show();
    }

    private void logoutButtonClicked() {
        final ProgressDialog dlg = new ProgressDialog(TravelsForGuests.this);
        dlg.setTitle("Please, wait a moment.");
        dlg.setMessage("Signing Out...");
        dlg.show();
        dlg.cancel();
        // logging out of Parse
        ParseUser.logOut();

        alertDisplayer("So you want to sign up?", "Please do that!");


    }


    private void view_all_buttonClicked() {


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
                            public void onClick(View v) {
                                ll.removeAllViews();

                                current_travel = Travel;

                                TextView title = new TextView(getApplicationContext());
                                title.setText("Travel Title:");
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
                                ImageView image = new ImageView(TravelsForGuests.this);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 720);

                                params1.gravity = Gravity.CENTER;
                                image.setLayoutParams(params1);


                                ParseFile imagex = Travel.getParseFile("imagex");
                                String imageUrl = imagex.getUrl();//live url
                                Uri imageUri = Uri.parse(imageUrl);
                                ll.addView(image);
                                Picasso.with(TravelsForGuests.this).load(imageUri.toString()).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(image);


//                                        ImageView image = findViewById(R.id.imageView);
//                                        ParseFile imagex = Travel.getParseFile("imagex");
//                                        String imageUrl = imagex.getUrl() ;//live url
//                                        Uri imageUri = Uri.parse(imageUrl);
//                                        Picasso.with(AdminPanel.this).load(imageUri.toString()).into(image);


                            }
                        });


                    }


                    Log.d("score", "Retrieved " + TravelsList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


    }


    private void btnDatePickerClicked() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String datas = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        String pattern = "dd-MM-yyyy";
                        Date date1 = new Date();

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                        try {
                            date1 = simpleDateFormat.parse(datas);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }


                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");


                        final Date finalDate = date1;
                        query.getInBackground(current_travel.getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject Travel, ParseException e) {
                                if (e == null) {

                                    Travel.put("date", finalDate);
                                    Travel.saveInBackground();
                                    view_all_buttonClicked();
                                }
                            }
                        });


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}






