package domeny.niema.klient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Raports extends AppCompatActivity {
    ParseObject current_travel;

    private Button genraports;




    private int mYear, mMonth, mDay;

    private Button back;
    private View.OnClickListener backOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backButtonClicked();
        }
    };


    //Aleks DO TESTOW UTWORZONE METODY
    //logout button listener
    private Button logout_button;
    private View.OnClickListener logout_buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logoutButtonClicked();
        }
    };


    private TextView monthgains;
    private TextView monthreservations;


    private View.OnClickListener genRaportsOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                genraportsButtonClicked();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raports);
        logout_button = findViewById(R.id.logout_button);


        back = findViewById(R.id.back);
        genraports = findViewById(R.id.generate_raport);

        monthgains = findViewById(R.id.monthgains);
        monthreservations = findViewById(R.id.monthreservations);






        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()


        );


//ustawianie listenerow

        logout_button.setOnClickListener(logout_buttonOnClickListener);


        back.setOnClickListener(backOnClickListener);
        genraports.setOnClickListener(genRaportsOnclickListener);


        gains();
    }


    private void gains() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Date date = calendar.getTime();
        Calendar calendarend = Calendar.getInstance();
        calendarend.set(Calendar.DAY_OF_MONTH, 1);
        calendarend.set(Calendar.MONTH, date.getMonth() + 1);
        Date dateend = calendarend.getTime();
        ParseQuery<ParseObject> queryx = ParseQuery.getQuery("Reservations");
        System.out.println(dateend);

        queryx.whereGreaterThanOrEqualTo("createdAt", date);
        queryx.whereLessThan("createdAt", dateend);
        queryx.findInBackground(new FindCallback<ParseObject>() {
            int gained = 0;
            int ilerezerw = 0;

            @Override
            public void done(List<ParseObject> Zarezerwowane, ParseException e) {
                for (final ParseObject rezerwy : Zarezerwowane) {
                    ilerezerw++;
                    gained = gained + rezerwy.getNumber("price").intValue();


                }

                monthreservations.setText(String.valueOf(ilerezerw));
                monthgains.setText(String.valueOf(gained + "$"));
            }
        });
    }





    private void logoutButtonClicked() {
        final ProgressDialog dlg = new ProgressDialog(Raports.this);
        dlg.setTitle("Please, wait a moment.");
        dlg.setMessage("Signing Out...");
        dlg.show();
        dlg.cancel();
        // logging out of Parse
        ParseUser.logOut();
        Intent intent = new Intent(Raports.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    private void genraportsButtonClicked() throws IOException {
        if (CheckInternet.isOnline()) {
            Context context = getApplicationContext();
            File path = context.getExternalFilesDir(null);
            final File raport = new File(path, "raport.txt");


            if (raport.exists()) {
                raport.delete();
                try {
                    raport.createNewFile();
                } catch (IOException e) {
                    Toast.makeText(Raports.this, "Couldn't create raport, check your permissions/free space!!!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            final FileWriter fw = new FileWriter(raport, true);


            //dopisujemy gainy z tego miesiaca i rezerwacje


            String monthgainy = monthgains.getText().toString();
            String monthrezerwacje = monthreservations.getText().toString();
            String gainsandrezerwacjeline = "This month gains:;" + monthgainy + ";;This month reservations:;" + monthrezerwacje + "\n\n";
            try {
                fw.write(gainsandrezerwacjeline);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            //dopisujemy total gainy
            ParseQuery<ParseObject> queryx = ParseQuery.getQuery("Reservations");


            //dodac to do funkcji i zwrocic do tablicy 2 elementowej

            queryx.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> rezerwacje, ParseException e) {
                    int totalgains = 0;
                    int totalrezerwacje = 0;

                    for (ParseObject rezerwacja : rezerwacje) {
                        totalgains = totalgains + rezerwacja.getNumber("price").intValue();
                        totalrezerwacje++;

                    }

                    String gainsandrezerwacjeline = "Total gains:;" + totalgains + "$;;All reservations:;" + totalrezerwacje;
                    try {
                        fw.write(gainsandrezerwacjeline);
                        // Toast.makeText(Raports.this, "Raport created!", Toast.LENGTH_LONG).show();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            //this month reservations
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            Date date = calendar.getTime();
            Calendar calendarend = Calendar.getInstance();
            calendarend.set(Calendar.DAY_OF_MONTH, 1);
            calendarend.set(Calendar.MONTH, date.getMonth() + 1);
            Date dateend = calendarend.getTime();
            ParseQuery<ParseObject> queryxz = ParseQuery.getQuery("Reservations");
            System.out.println(dateend);

            queryxz.whereGreaterThanOrEqualTo("createdAt", date);
            queryxz.whereLessThan("createdAt", dateend);
            queryxz.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> Zarezerwowane, ParseException e) {
                    try {
                        fw.write("\nThis month reservations:\n");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    for (final ParseObject rezerwy : Zarezerwowane) {
                        String thismonthreservations = "\nUsername:; " + rezerwy.getString("username") + ";;" + rezerwy.getString("travel_name") + ";;"
                                + "Purchased at:; " + rezerwy.getCreatedAt() + ";;" + "Current status:; " + rezerwy.getString("status\n");
                        try {
                            fw.write(thismonthreservations);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }


                                }
                            });

//all reservations
            try {
                fw.write("\nAll reservations:\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            queryx.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> rezerwacje, ParseException e) {
                    int totalgains = 0;
                    int totalrezerwacje = 0;

                    for (ParseObject rezerwacja : rezerwacje) {
                        totalgains = totalgains + rezerwacja.getNumber("price").intValue();
                        totalrezerwacje++;

                    }

                    String gainsandrezerwacjeline = "Total gains:;" + totalgains + "$;;All reservations:;" + totalrezerwacje;
                    try {
                        fw.write(gainsandrezerwacjeline);
                        // Toast.makeText(Raports.this, "Raport created!", Toast.LENGTH_LONG).show();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    //rezerwacje z tego miesiaca


                }
            });


            queryx.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> Userzy, ParseException e) {
                    try {
                        fw.write("\nAll users:\n");
                    } catch (IOException e1) {
                        e1.printStackTrace();
//dopisujemy userow

                        for (ParseObject user : Userzy) {


                            String userline = "\nUser:;" + user.getString("username") + ";;Registered at:;" + user.getCreatedAt().toString() + "\n";
                            try {
                                fw.write(userline);
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }

                        }
                    }
                }
                            });


            try {
                fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                Toast.makeText(Raports.this, "Couldn't close raport file, please contact with your Developer", Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(Raports.this, "No internet connection, try again", Toast.LENGTH_LONG).show();

        }
    }


    private void backButtonClicked() {
        Intent intent = new Intent(Raports.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



}






