package com.example.kamrankhan.pstapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.example.kamrankhan.pstapplication.PST.PSTException;
import com.example.kamrankhan.pstapplication.PST.PSTFile;
import com.example.kamrankhan.pstapplication.PST.PSTFolder;
import com.example.kamrankhan.pstapplication.PST.PSTMessage;





public class activity_subchildfolder extends AppCompatActivity {

    //1- Sender Name
    String sendername;
    //2- Sender Email
    String senderemail;
    //3- Subject
    String subject;
    //4- Date
    SimpleDateFormat DATE_FORMAT;
    String date;
    //5- Message
    String msg;
    //6- CC
    String cc;
    //7- BCC
    String bcc;

    Context mCtx;
    public static final String FileName = "kamideegreat@gmail.com.pst";
    public static final String FileLocation = "data/data/com.example.kamrankhan.pstapplication/files";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subchildfolder);
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_Top);
        setSupportActionBar(toolbarTop);

        //Code for Bottom Toolbar
        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_Bottom);
        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){

                    case R.id.id_reply:
                        Toast.makeText(getApplicationContext(),"Reply",Toast.LENGTH_SHORT).show();
                        Intent reply = new Intent(activity_subchildfolder.this, activity_reply_email.class);

                        String replyText="Reply";
                        reply.putExtra("replytxt", replyText);
                        startActivity(reply);

                        break;

                    case R.id.id_reply_all:
                        Toast.makeText(getApplicationContext(),"Reply All",Toast.LENGTH_SHORT).show();
                        Intent replyall = new Intent(activity_subchildfolder.this, activity_replyall_email.class);

                        String replyallText="Reply All";
                        replyall.putExtra("replyalltxt", replyallText);
                        startActivity(replyall);
                        break;

                    case R.id.id_forward:
                        Toast.makeText(getApplicationContext(),"Forward",Toast.LENGTH_SHORT).show();
                        Intent forward = new Intent(activity_subchildfolder.this, activity_forward_email.class);

                        String forwardText="Forward";
                        forward.putExtra("forwardtxt", forwardText);
                        startActivity(forward);
                        break;

                    case R.id.id_compose:
                        Toast.makeText(getApplicationContext(),"Compose",Toast.LENGTH_SHORT).show();
                        Intent compose = new Intent(activity_subchildfolder.this, activity_compose_email.class);

                        String composeText="Compose";
                        compose.putExtra("composetxt", composeText);
                        startActivity(compose);

                        break;


                    default:
                        break;
                }
                return true;
            }
        });
        // Inflate a menu to be displayed in the toolbar
        toolbarBottom.inflateMenu(R.menu.menu_subchildfolder);


        //CODE FOR BOTTOM BAR ENDS HERE

        //CODE FOR BACK BUTTON ON ACTION BAR
        getSupportActionBar ( ).setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ( ).setHomeAsUpIndicator ( R.drawable.previous );

        //Getting the value of clicked item via intent and storing its value in foldervalue variable
        String foldervalue=getIntent().getExtras().getString("folderName");
        //Sender Name, Subject and Date
        String senderValue=getIntent().getExtras().getString("subSender");
        String subjectValue=getIntent().getExtras().getString("subSubject");
        String senderDate=getIntent().getExtras().getString("subDate");

        //Setting Actionbar Title with the value of senderValue(Sender Name) variable
        setTitle(foldervalue+" | "+senderValue);


        TextView tvLabelSender=(TextView)findViewById(R.id.tv_labelsender);
        TextView tvSenderName=(TextView)findViewById(R.id.tv_sendername);
        TextView tvLabelSubject=(TextView)findViewById(R.id.tv_labelsubject);
        TextView tvSubject=(TextView)findViewById(R.id.tv_subject);
        TextView tvLabelDate=(TextView)findViewById(R.id.tv_labeldate);
        TextView tvDate=(TextView)findViewById(R.id.tv_date);
        WebView wvMessageBodyHtml=(WebView)findViewById(R.id.wv_msgbody);


        wvMessageBodyHtml.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvMessageBodyHtml.getSettings().setLoadWithOverviewMode(true);

        wvMessageBodyHtml.getSettings().setLoadWithOverviewMode(true);
        wvMessageBodyHtml.getSettings().setDisplayZoomControls(false);



        //code for javalib pst
        try {

            //PSTFile pstFile = new PSTFile("data/data/com.example.kamrankhan.pstapplication/files/kamideegreat@gmail.com.pst");

            //PSTFile pstFile = new PSTFile("data/data/com.example.kamrankhan.pstapplication/kamideegreat@gmail.com.pst");
            PSTFile pstFile = new PSTFile("data/data/com.example.kamrankhan.pstapplication/files/kamideegreat@gmail.com.pst");

            // display the name of the header i.e. kamideegreat@gmail.com
            //System.out.println(pstFile.getMessageStore().getDisplayName());

            // for getting the root folder of MS Outlook
            PSTFolder pstFolder = pstFile.getRootFolder();

            Vector<PSTFolder> folder = new Vector<PSTFolder>();

            // this will return all the subfolder under the root folder
            folder = pstFolder.getSubFolders();

            // this loop is used for processing all the folders of MS Outlook
            for(int i=0;i<pstFolder.getSubFolderCount();i++)
            {
                // for displaying the folder name (Main RootFolder i.e Top of Outook data file")
                //System.out.println("Display Folder Name->"+folder.get(i).getDisplayName());

                Vector<PSTFolder> personalFolder = new Vector<PSTFolder>();

                // for getting the folder inside root folder
                personalFolder = folder.get(i).getSubFolders();

                // this loop is used for processing all the folders under root folder
                for(int j=0;j<personalFolder.size();j++)
                {
                    PSTFolder currentFolder=personalFolder.get(j);

                    String folderName = currentFolder.getDisplayName();
                    String folderCount = String.valueOf(currentFolder.getContentCount());
                    long folderId = currentFolder.getDescriptorNodeId();


                    // display the name of the root folder i.e Inbox, Deleted Items etc
                    //System.out.println(folderName+" ("+folderCount+")");

                    //ADDING VALUES IN DATABASE
                    //if(folderName.equals("Inbox") || folderName.equals("Deleted Items") || folderName.equals("Drafts") || folderName.equals("Junk E-mail") || folderName.equals("Outbox") || folderName.equals("Contacts") || folderName.equals("Notes") || folderName.equals("Tasks")){
                    //create(folderId, folderName, " ("+folderCount+")");
                    //}


                    // this condition is used for checking the name of the folder
                    // if folder name is Inbox then it will enter in IF body
                    //This nested if condition is used to get emails inside root folders

                    if(personalFolder.get(j).getDisplayName().equalsIgnoreCase(folderName))
                    {
                        // for checking inbox folder has email or not
                        if(personalFolder.get(j).getContentCount() > 0)
                        {
                            // for getting the email message
                            PSTMessage email = (PSTMessage)personalFolder.get(j).getNextChild();

                            // if email is not null then it will enter into the loop
                            while (email != null)
                            {

                                long emailID = email.getDescriptorNodeId();  // for getting the email descriptor id which uniquely identify the email


                                //1- Sender Name
                                sendername=email.getSenderName();
                                //2- Sender Email
                                senderemail=email.getSenderEmailAddress();
                                //3- Subject
                                subject=email.getSubject();
                                //4- Date
                                DATE_FORMAT = new SimpleDateFormat("MM/dd/yy - hh:mm");
                                date = DATE_FORMAT.format(email.getMessageDeliveryTime());
                                //5- Message
                                msg=email.getBodyHTML();
                                //6-CC
                                cc=email.getDisplayCC();
                                //7-BCC
                                bcc=email.getDisplayBCC();

                                if(sendername.equals(senderValue) && (subject.equals(subjectValue)) && date.equals(senderDate)) {

                                    //Setting values in Respective Textviews
                                    tvSenderName.setText(sendername);  //Sender Name
                                    tvSubject.setText(subject);        //Subject of Email
                                    tvDate.setText(date);              //Date of Email
                                    wvMessageBodyHtml.loadData(msg, "text/html", "UTF-8");  //Message Body with Html

                                    //Getting the values of mail
                                    SharedPreferences emailObjects= this.getSharedPreferences("email", MODE_WORLD_READABLE);
                                    emailObjects.edit().putString("senderNametxt", sendername).commit();
                                    emailObjects.edit().putString("senderEmailtxt", senderemail).commit();
                                    emailObjects.edit().putString("sujectTxt", subject).commit();
                                    emailObjects.edit().putString("messageTxt", msg).commit();
                                    emailObjects.edit().putString("ccTxt", cc).commit();
                                    emailObjects.edit().putString("bccTxt", bcc).commit();
                                    emailObjects.edit().putString("dateTxt", date).commit();

                                    break;

                                }

                                email = (PSTMessage)personalFolder.get(j).getNextChild();
                            }

                        }
                    }
                    else
                    {
                        continue;
                    }


                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PSTException e) {
            e.printStackTrace();
        }

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
