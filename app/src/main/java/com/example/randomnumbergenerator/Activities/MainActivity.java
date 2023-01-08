package com.example.randomnumbergenerator.Activities;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.randomnumbergenerator.lib.Utils.getJSONStringFromNumberList;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.randomnumbergenerator.R;
import com.example.randomnumbergenerator.Models.RandomNumber;
import com.example.randomnumbergenerator.lib.Utils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private RandomNumber mRandomNumber;
    private ArrayList<Integer> mNumberHistory;
    private int randomNumber;
    private final String key = "GENERATOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRandomNumber = new RandomNumber();
        setupToolbar_andFAB();
        initializeHistoryList(savedInstanceState, key);


    }
    private void initializeHistoryList (Bundle savedInstanceState, String key)
    {
        if (savedInstanceState != null) {
            mNumberHistory = savedInstanceState.getIntegerArrayList (key);
        }
        else {
            String history = getDefaultSharedPreferences (this).getString (key, null);
            mNumberHistory = history == null ?
                    new ArrayList<> () : Utils.getNumberListFromJSONString (history);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor edit = getDefaultSharedPreferences(MainActivity.this).edit();
        edit.putString(key, getJSONStringFromNumberList(mNumberHistory));
        edit.apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(key, mNumberHistory);

    }


    private void setupToolbar_andFAB() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomNumber = retrieveRandomNumber();
                addAndSend(randomNumber);
            }
        });
    }
    private int retrieveRandomNumber(){
        EditText fromBar = (EditText) findViewById(R.id.currencyFromName);
        String from = fromBar.getText().toString();
        int f = Integer.parseInt(from);

        EditText toBar = (EditText) findViewById(R.id.currencyToName);
        String to = toBar.getText().toString();
        int t = Integer.parseInt(to);

        mRandomNumber.setFromTo(f, t);
        randomNumber = mRandomNumber.getCurrentRandomNumber();
        return randomNumber;
    }
    private void addAndSend(int rn){
        mNumberHistory.add(rn);
        TextView resultView = findViewById(R.id.result);
        resultView.setText(String.valueOf(randomNumber));
    }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_history) {
                showHistory();
                return true;
            }
            if(id == R.id.action_clear){
                mNumberHistory.clear();
                return true;
            }if (id == R.id.action_about){
                showAbout();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

    private void showHistory() {
        Utils.showInfoDialog(MainActivity.this, getString(R.string.gen_nums), mNumberHistory.toString());
    }

    private void showAbout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
            Snackbar.make(toolbar, getString(R.string.about), Snackbar.LENGTH_SHORT).show();
        }
    }


