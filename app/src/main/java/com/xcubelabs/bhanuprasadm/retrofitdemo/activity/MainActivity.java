package com.xcubelabs.bhanuprasadm.retrofitdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xcubelabs.bhanuprasadm.retrofitdemo.R;
import com.xcubelabs.bhanuprasadm.retrofitdemo.api.GitApi;
import com.xcubelabs.bhanuprasadm.retrofitdemo.model.GitModel;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    Button click;
    TextView tv;
    EditText edit_user;
    ProgressBar pbar;
    String API = "https://api.github.com";    //BASE URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        click = (Button) findViewById(R.id.btnSearch);
        tv = (TextView) findViewById(R.id.tvData);
        edit_user = (EditText) findViewById(R.id.etSearch);
        pbar = (ProgressBar) findViewById(R.id.pg);
        pbar.setVisibility(View.INVISIBLE);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edit_user.getText().toString();
                pbar.setVisibility(View.VISIBLE);

                //Retrofit section start from here...
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(API).build();           //create an adapter for retrofit with base url

                GitApi git = restAdapter.create(GitApi.class);
                //creating a service for adapter with our GET class
                //Now ,we need to call for response
                //Retrofit using gson for JSON-POJO conversion

                git.getFeed(user, new Callback<GitModel>() {
                    @Override
                    public void success(GitModel gitmodel, Response response) {
                        tv.setText("Github Name \t:" + gitmodel.getName() + "\nLocation \t\t:" + gitmodel.getLocation() + "\nRepos \t\t\t:" + gitmodel.getPublicRepos());
                        pbar.setVisibility(View.INVISIBLE);       //disable progressbar
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        tv.setText(error.getMessage());
                        pbar.setVisibility(View.INVISIBLE);       //disable progressbar
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
