package com.xcubelabs.bhanuprasadm.retrofitdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xcubelabs.bhanuprasadm.retrofitdemo.R;
import com.xcubelabs.bhanuprasadm.retrofitdemo.api.GitApi;
import com.xcubelabs.bhanuprasadm.retrofitdemo.model.GitModel;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton click;
    Button btnShare;
    TextView tv;
    EditText edit_user;
    ProgressBar pbar;
    String API = "https://api.github.com";    //BASE URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        click = (ImageButton) findViewById(R.id.btnSearch);
        tv = (TextView) findViewById(R.id.tvData);
        edit_user = (EditText) findViewById(R.id.etSearch);
        pbar = (ProgressBar) findViewById(R.id.pg);
        btnShare = (Button) findViewById(R.id.btnShare);
        pbar.setVisibility(View.INVISIBLE);

        btnShare.setOnClickListener(this);
        click.setOnClickListener(this);
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
        if(id == R.id.menu_to_webview){
            startActivity(new Intent(MainActivity.this, WebViewActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSearch:
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
                break;
            case R.id.btnShare:
                if(!TextUtils.isEmpty(tv.getText())){
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, tv.getText());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
                break;
            default:
                break;
        }
    }
}
