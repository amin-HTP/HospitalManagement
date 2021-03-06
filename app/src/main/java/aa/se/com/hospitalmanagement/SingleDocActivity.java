package aa.se.com.hospitalmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsLayoutInflater;

public class SingleDocActivity extends AppCompatActivity {

    private Typeface yekanFont;
    private static String name;
    private int image;
    private int rate;
    private TextView textViewName;
    private TextView textViewDes;
    private ImageView imageView;
    private RatingBar ratingBar;
    private String completeDes;
    private LinearLayout resButContainer;
    private Button reserveButton;
    private static PrefManager prefManager;
    private RelativeLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_doc);


        prefManager = new PrefManager(this);
        yekanFont = Typeface.createFromAsset(getAssets(), "fonts/b_yekan.ttf");
        name = getIntent().getStringExtra("NAME");
        image = getIntent().getIntExtra("IMAGE", R.drawable.ic_doc);
        completeDes = getIntent().getStringExtra("COMDES");
        rate = getIntent().getIntExtra("RATE", 0);

        textViewName = (TextView) findViewById(R.id.text_doc);
        textViewDes = (TextView) findViewById(R.id.text_doc_des);

        imageView = (ImageView) findViewById(R.id.image_doc);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        resButContainer = (LinearLayout) findViewById(R.id.linearLayout_buttonContainer);
        rootLayout = (RelativeLayout) findViewById(R.id.layout_root);
        reserveButton = (Button) findViewById(R.id.button_reservation);

        textViewName.setTypeface(yekanFont);
        textViewDes.setTypeface(yekanFont);
        reserveButton.setTypeface(yekanFont);
        textViewName.setText(name);
        textViewDes.setText(completeDes);
        imageView.setImageResource(image);
        ratingBar.setRating(rate);

        textViewDes.animate()
                .alpha(1).setDuration(1000);

        resButContainer.animate()
                .alpha(1).setDuration(1000);

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.getUserHasRegistered()) {
                    new ReservationDialog().show(getSupportFragmentManager(), "reservationDialog");
                } else {
                    Snackbar snackbar = Snackbar.make(rootLayout, "باید ثبت نام را کامل  کنید.", Snackbar.LENGTH_LONG).setAction("تکمیل ثبت نام", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(SingleDocActivity.this, Profile.class);
                            startActivity(intent);
                        }
                    });
                    snackbar.show();

                    snackbar.setActionTextColor(Color.WHITE);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    sbView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
        });

    }

    public static void writeDocNameToSP(int i) {
        prefManager.setDoctorName(name, i);
    }
}
