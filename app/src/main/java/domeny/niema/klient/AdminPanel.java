package domeny.niema.klient;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminPanel extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    ParseObject current_travel;
    private Button mod_date;
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
    private Button modify;

    //view button listener
//modyfikuj obrazek button listener
    private Button mod_image;
    private View.OnClickListener mod_imageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mod_imageButtonClicked();
        }
    };

    //modify button listener
    private Button mod_summary;
    private Button mod_price;
    private Button mod_title;
    private View.OnClickListener modifyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            modifyButtonClicked();
        }
    };
    private LinearLayout ll;
    private View.OnClickListener mod_dateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnDatePickerClicked();
        }
    };
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
    private View.OnClickListener mod_summaryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mod_summaryButtonClicked();
        }
    };
    private View.OnClickListener mod_priceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mod_priceButtonClicked();
        }
    };
    private View.OnClickListener mod_titleOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mod_titleButtonClicked();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        logout_button = findViewById(R.id.logout_button);
        view_all = findViewById(R.id.view_all);
        modify = findViewById(R.id.modify);
        mod_image = findViewById(R.id.mod_image);
        mod_summary = findViewById(R.id.mod_summary);
        mod_price = findViewById(R.id.mod_price);
        mod_title = findViewById(R.id.mod_title);
        info_button = findViewById(R.id.info);
        mod_date = findViewById(R.id.datepicker);
        modify.setEnabled(false);

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
        modify.setOnClickListener(modifyOnClickListener);
        mod_image.setOnClickListener(mod_imageOnClickListener);
        mod_summary.setOnClickListener(mod_summaryOnClickListener);
        mod_price.setOnClickListener(mod_priceOnClickListener);
        mod_title.setOnClickListener(mod_titleOnClickListener);
        info_button.setOnClickListener(info_buttonOnClickListener);
        mod_date.setOnClickListener(mod_dateOnClickListener);


    }

    private void info_buttonClicked() {
        ll.removeAllViews();
        modify.setEnabled(false);
        mod_image.setVisibility(View.INVISIBLE);
        mod_summary.setVisibility(View.INVISIBLE);
        mod_price.setVisibility(View.INVISIBLE);
        mod_title.setVisibility(View.INVISIBLE);
        mod_date.setVisibility(View.INVISIBLE);

        TextView title = new TextView(getApplicationContext());
        title.setText("Welcome to Admin Panel");
        title.setTypeface(null, Typeface.BOLD);
        title.setTextSize(18);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 100);
        params1.gravity = Gravity.CENTER;
        title.setLayoutParams(params1);
        ll.addView(title);

        TextView paneldescription = new TextView(getApplicationContext());
        paneldescription.setText("There you can manage your travels. You can:\n- add new travels,\n- modify current travels (change their title, description, date or image),\n- show actual reservations and accept/cancel them.\n");
        ll.addView(paneldescription);

        TextView howtomodify = new TextView(getApplicationContext());
        howtomodify.setText("How to modify travels:");
        howtomodify.setTypeface(null, Typeface.BOLD);
        howtomodify.setTextSize(15);
        ll.addView(howtomodify);

        TextView howtomodify2 = new TextView(getApplicationContext());
        howtomodify2.setText("Firstly click button - 'View Travels', next you should pick travel you want to modify. Then click 'modify' button - so modify options could appear.");
        ll.addView(howtomodify2);

    }


    private void alertDisplayer(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPanel.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        Intent intent = new Intent(AdminPanel.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }


                });


        AlertDialog ok = builder.create();
        ok.show();
    }

    private void logoutButtonClicked() {
        final ProgressDialog dlg = new ProgressDialog(AdminPanel.this);
        dlg.setTitle("Please, wait a moment.");
        dlg.setMessage("Signing Out...");
        dlg.show();
        dlg.cancel();
        // logging out of Parse
        ParseUser.logOut();

        alertDisplayer("So, you're going...", "Ok...Bye-bye then");
    }


    private void view_all_buttonClicked() {


        mod_image.setVisibility(View.INVISIBLE);
        mod_summary.setVisibility(View.INVISIBLE);
        mod_price.setVisibility(View.INVISIBLE);
        mod_title.setVisibility(View.INVISIBLE);
        mod_date.setVisibility(View.INVISIBLE);

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


                        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            b.setBackgroundDrawable(roundDrawable);
                        } else {
                            b.setBackground(roundDrawable);
                        }


                        ll.addView(b);
                        TextView spacja0 = new TextView(getApplicationContext());
                        ll.addView(spacja0);
                        b.setId(i);
                        i++;

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ll.removeAllViews();
                                modify.setEnabled(true);
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
                                ImageView image = new ImageView(AdminPanel.this);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(720, 405);

                                params1.gravity = Gravity.CENTER;
                                image.setLayoutParams(params1);


                                ParseFile imagex = Travel.getParseFile("imagex");
                                String imageUrl = imagex.getUrl();//live url
                                Uri imageUri = Uri.parse(imageUrl);
                                ll.addView(image);
                                Picasso.with(AdminPanel.this).load(imageUri.toString()).into(image);


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

    private void modifyButtonClicked() {

        // TITLE
        mod_title.setVisibility(View.VISIBLE);

// SUMMARY
        mod_summary.setVisibility(View.VISIBLE);

// PRICE
        mod_price.setVisibility(View.VISIBLE);

// IMAGE
        mod_image.setVisibility(View.VISIBLE);
//DATE
        mod_date.setVisibility(View.VISIBLE);


    }


    private void mod_imageButtonClicked() {

        openGallery();

    }

    private void openGallery() {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri selectedImageUri = data.getData();

            // GET IMAGE PATH
            String imagePath = selectedImageUri.getPath();

            // IMAGE NAME
            String imageName = imagePath.substring(imagePath.lastIndexOf("/"));

            InputStream is = null;
            try {
                is = getContentResolver().openInputStream(selectedImageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Bitmap bitmap = BitmapFactory.decodeStream(is);
            Bitmap bitmap = ScaleBitmap(selectedImageUri, AdminPanel.this, 720);
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Compress image to lower quality scale 1 - 100
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);


            final byte[] image = stream.toByteArray();
            final ParseFile image_ready = new ParseFile("r.png", image);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");


            query.getInBackground(current_travel.getObjectId(), new GetCallback<ParseObject>() {
                public void done(ParseObject Travel, ParseException e) {
                    if (e == null) {

                        Travel.put("imagex", image_ready);

                        final ProgressDialog dlg = new ProgressDialog(AdminPanel.this);
                        dlg.setTitle("Please, wait a moment.");
                        dlg.setMessage("Uploading...");
                        dlg.show();
                        Travel.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {

                                    dlg.dismiss();
                                    view_all_buttonClicked();
                                } else {

                                    dlg.dismiss();
                                    ParseUser.logOut();
                                    Toast.makeText(AdminPanel.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    view_all_buttonClicked();
                                }
                            }
                        });


                    }

                }
            });


        }
    }

    private Bitmap ScaleBitmap(Uri imageUri, Context context, int maxImageSideLength) {
        ContentResolver resolver = context.getContentResolver();
        InputStream is;
        try {
            is = resolver.openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            Log.e("Image not found.", "error");
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, opts);

        // scale the image
        float maxSideLength = maxImageSideLength;
        float scaleFactor = Math.min(maxSideLength / opts.outWidth, maxSideLength / opts.outHeight);
        // do not upscale!
        if (scaleFactor < 1) {
            opts.inDensity = 10000;
            opts.inTargetDensity = (int) ((float) opts.inDensity * scaleFactor);
        }
        opts.inJustDecodeBounds = false;

        try {
            is.close();
        } catch (IOException e) {
            // ignore
        }
        try {
            is = resolver.openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            Log.e("Image not found.", "error");
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
        try {
            is.close();
        } catch (IOException e) {
            // ignore
        }

        return bitmap;
    }

    private void mod_summaryButtonClicked() {

        AlertDialog.Builder alert = new AlertDialog.Builder(AdminPanel.this);

        alert.setTitle("Travel summary change");
        alert.setMessage("Set new summary:");

// Set an EditText view to get user input
        final EditText input = new EditText(getApplicationContext());
        alert.setView(input);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");

// Retrieve the object by id
                query.getInBackground(current_travel.getObjectId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject Travel, ParseException e) {
                        if (e == null) {

                            Travel.put("summary", input.getText().toString());
                            Travel.saveInBackground();
                            view_all_buttonClicked();
                        }
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }


    private void mod_priceButtonClicked() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AdminPanel.this);

        alert.setTitle("New Travel price");
        alert.setMessage("Set new price:");

// Set an EditText view to get user input
        final EditText input = new EditText(getApplicationContext());
        alert.setView(input);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");

// Retrieve the object by id
                query.getInBackground(current_travel.getObjectId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject Travel, ParseException e) {
                        if (e == null) {
                            int number = Integer.parseInt(input.getText().toString());
                            Travel.put("price", number);
                            Travel.saveInBackground();
                            view_all_buttonClicked();
                        }
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void mod_titleButtonClicked() {


        AlertDialog.Builder alert = new AlertDialog.Builder(AdminPanel.this);

        alert.setTitle("Travel title change");
        alert.setMessage("Set new title:");

// Set an EditText view to get user input
        final EditText input = new EditText(getApplicationContext());
        alert.setView(input);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");

// Retrieve the object by id
                query.getInBackground(current_travel.getObjectId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject Travel, ParseException e) {
                        if (e == null) {

                            Travel.put("title", input.getText().toString());
                            Travel.saveInBackground();
                            view_all_buttonClicked();
                        }
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
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






