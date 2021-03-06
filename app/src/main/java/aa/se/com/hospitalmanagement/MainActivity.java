package aa.se.com.hospitalmanagement;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsLayoutInflater;

public class MainActivity extends AppCompatActivity {

    private Typeface yekanFont;
    private RecyclerView recyclerView;
    ListView navDrawer;
    TextView toolbar_title;
    TextView toolbar_price;
    TextView toolbar_badge;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    int c = 0;
    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Enable automatic xml icons detection
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        prefManager = new PrefManager(this);

        if (!prefManager.getUserHasBeenAuthenticated()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        }

        yekanFont = Typeface.createFromAsset(getAssets(), "fonts/b_yekan.ttf");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_content);
        navDrawer = (ListView) findViewById(R.id.listView_navDrawer);
        toolbar_title = (TextView) findViewById(R.id.textView_toolbar_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                syncState();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(false);  //hide actionBar title

        toolbar_title.setTypeface(yekanFont);

        navDrawer.setAdapter(new NavDrawerAdapter(this, getResources().getStringArray(R.array.navDrawer), yekanFont));
        navDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, History.class);
                switch (position) {
                    case 1:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        startActivity(intent);
                        break;
                    case 3:
                        startActivity(intent);
                        break;
                    case 4:
                        Intent intent2 = new Intent(MainActivity.this, Profile.class);
                        startActivity(intent2);
//                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 5:
                        Toast.makeText(MainActivity.this, getString(R.string.exit), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        MainActivity_Recyclerview_Adapter adapter = new MainActivity_Recyclerview_Adapter(recyclerView.getContext(), yekanFont);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        if (toolbar_badge != null) {
            updateBadge(String.valueOf(prefManager.getReservationNumber()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        // Inflate the menu_options; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        MenuItem badge = menu.findItem(R.id.item_badge);
        MenuItem price = menu.findItem(R.id.item_price);

        toolbar_price = (TextView) price.getActionView().findViewById(R.id.textView_menuItem);
        toolbar_price.setTypeface(yekanFont);
        toolbar_badge = (TextView) badge.getActionView().findViewById(R.id.menu_badge);
        toolbar_badge.setTypeface(yekanFont);

        updateBadge(String.valueOf(prefManager.getReservationNumber()));

        badge.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, History.class);
                startActivity(intent);
            }
        });

        badge.getActionView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                prefManager.setUserHasBeenAuthenticated(false);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Animation anim_error = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_anim);

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.cart) {
//            return true;
//        } else
        if (id == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void updateBadge(String str) {
        toolbar_badge.setText(str);
    }

    public void updatePrice(String str) {
        toolbar_price.setText(str);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
