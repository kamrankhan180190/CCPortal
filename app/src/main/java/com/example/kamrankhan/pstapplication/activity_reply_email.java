package com.example.kamrankhan.pstapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class activity_reply_email extends AppCompatActivity {

    //Declaring EditText
    private EditText editTextEmailTo;
    private EditText editTextEmailCC;
    private EditText editTextEmailBCC;
    private EditText editTextSubject;
    private EditText editTextMessage;

    //Variables for email reply contents
    String senderNameTxt;
    String senderEmailTxt;
    String subjectTxt;
    String messageTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Getting the values for email from sharedPreferences
        SharedPreferences emailObjects= getSharedPreferences("email", MODE_WORLD_READABLE);
        senderNameTxt= emailObjects.getString("senderNametxt", null);
        senderEmailTxt= emailObjects.getString("senderEmailtxt", null);
        subjectTxt= emailObjects.getString("sujectTxt", null);
        messageTxt= emailObjects.getString("messageTxt", null);

        //Initializing the views
        editTextEmailTo = (EditText) findViewById(R.id.editTextTo);
        editTextEmailCC = (EditText) findViewById(R.id.editTextCC);
        editTextEmailBCC = (EditText) findViewById(R.id.editTextBCC);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        //Setting content for email
        editTextEmailTo.setText(senderEmailTxt);
        editTextEmailCC.setText("");
        editTextEmailBCC.setText("");
        editTextSubject.setText("RE: "+subjectTxt);
        //editTextMessage.setText(messageTxt);

        /*
        if(bccTxt.equals(" ") || ccTxt.equals(" ") || bccTxt.equals(" ") && ccTxt.equals(" ") ){

            editTextEmailCC.setText("");
            editTextEmailBCC.setText("");
        }
        else{

            editTextEmailCC.setText(bccTxt);
            editTextEmailBCC.setText(ccTxt);

        }
        */


        //Code for Buttons on Toolbar
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){

                    case R.id.id_send:
                        Toast.makeText(getApplicationContext(),"Send",Toast.LENGTH_SHORT).show();
                        sendEmail();
                        break;

                    case R.id.id_cancel:
                        Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_SHORT).show();
                        editTextEmailTo.setText("");
                        editTextEmailCC.setText("");
                        editTextEmailBCC.setText("");
                        editTextSubject.setText("");
                        editTextMessage.setText("");
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_mail);

        //CODE FOR BACK BUTTON ON ACTION BAR
        getSupportActionBar ( ).setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ( ).setHomeAsUpIndicator ( R.drawable.previous );

        //final String sendType=getIntent().getExtras().getString("composetxt");
        //setTitle(sendType);

    }

    private void sendEmail() {
        //Getting content for email
        String emailto = editTextEmailTo.getText().toString().trim();
        String emailcc = editTextEmailCC.getText().toString().trim();
        String emailbcc = editTextEmailBCC.getText().toString().trim();
        String subject = editTextSubject.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        //Creating SendMail object
        SendMail sm = new SendMail(this, emailto, emailcc, emailbcc, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mail, menu);
        MenuItem item = menu.findItem(R.id.id_send);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            default:
                //finish() is for back button
                finish();
                break;
        }
        return true;
    }

}
