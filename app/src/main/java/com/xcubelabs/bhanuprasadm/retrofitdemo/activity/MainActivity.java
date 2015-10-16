package com.xcubelabs.bhanuprasadm.retrofitdemo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xcubelabs.bhanuprasadm.retrofitdemo.R;
import com.xcubelabs.bhanuprasadm.retrofitdemo.adapter.ShareIntentListAdapter;
import com.xcubelabs.bhanuprasadm.retrofitdemo.api.GitApi;
import com.xcubelabs.bhanuprasadm.retrofitdemo.model.GitModel;

import java.util.List;

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
    ShareIntentListAdapter objShareIntentListAdapter;
    Context context;
    GitModel userDetails;
    private String ShortDesc = "Short Description";
    private String BusinessName = "PurpleTalk";

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

        context = this;
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
                        tv.setText(gitmodel.shortDescription());
                        pbar.setVisibility(View.INVISIBLE);       //disable progressbar
                        userDetails = gitmodel;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        tv.setText(error.getMessage());
                        pbar.setVisibility(View.INVISIBLE);       //disable progressbar
                    }
                });
                break;
            case R.id.btnShare:
                if (userDetails != null) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    PackageManager pm = getPackageManager();
                    List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);

                    objShareIntentListAdapter = new ShareIntentListAdapter(MainActivity.this,activityList.toArray());
                    // Create alert dialog box
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Share via");
                    builder.setAdapter(objShareIntentListAdapter, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int item) {
                            ResolveInfo info = (ResolveInfo) objShareIntentListAdapter.getItem(item);
                            // start respective activity
                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                            intent.setType("text/plain");
                            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Github Details of " + userDetails.getLogin());
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, userDetails.shortDescription());
                            intent.putExtra(Intent.EXTRA_TITLE, userDetails.getLogin());
                            context.startActivity(intent);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;
            default:
                break;
        }
    }
}
