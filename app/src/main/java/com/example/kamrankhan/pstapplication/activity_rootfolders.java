package com.example.kamrankhan.pstapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;




public class activity_rootfolders extends AppCompatActivity {

    private ListView lvRootFolders;
    private DbAdapter dbHelper;
    private RootFolderListAdapter adapter;
    private ArrayList<RootFolders> mRootFolderList;
    final Context context = this;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    int channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rootfolders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        channel = (sharedpreferences.getInt("id", channel));

        //Open DATABASE
        dbHelper = new DbAdapter(this);
        dbHelper.open();

        //Add data into DATABASE if not null
        dbHelper.insertFromPST();


        mRootFolderList=new ArrayList<>();

        //Get Data from DATABASE
        fetchFromDB();

        adapter=new RootFolderListAdapter(getApplicationContext(),mRootFolderList);
        lvRootFolders=(ListView)findViewById(R.id.listView_Root);
        lvRootFolders.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvRootFolders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Extracting and Saving the value of the clicked item in TextView variable
                TextView tvrootfolders=(TextView)view.findViewById(R.id.tv_rootfolder); //now tvrootfolders contain the value of clicked item

                //listview clicked item index
                int itemPosition = position;

                //Listview clicked item value
                String itemValue = tvrootfolders.getText().toString();

                Toast.makeText(getApplicationContext(),itemValue,Toast.LENGTH_LONG).show();
                //if(position==5) {
                    Intent i = new Intent(activity_rootfolders.this, activity_childfolders.class);
                    i.putExtra("rootFolderName", itemValue);
                    startActivity(i);
                //}
            }
        });

        //Long Click Listener
        lvRootFolders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                //Extracting and Saving the value of the clicked item in TextView variable
                TextView tvFolderName=(TextView)view.findViewById(R.id.tv_rootfolder);

                //Listview clicked item value
                String itemNameFolder = tvFolderName.getText().toString();

                //Method
                removeItemFromList(itemNameFolder);
                //adapter.notifyDataSetChanged();

                return true;

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Clears Lisview from Data
        mRootFolderList.clear();
        //Fetches data from DB and populates Listview
        fetchFromDB();
        adapter.notifyDataSetChanged();
    }



    public void fetchFromDB(){

        Cursor cursor =dbHelper.QueryData("SELECT name FROM sqlite_master WHERE type='table'");
        if(cursor !=null){
            if(cursor.moveToFirst()){
                do{
                    String folderName = cursor.getString(0);
                    Cursor cursor2 =dbHelper.QueryData("SELECT * FROM "+folderName);
                    int count = cursor2.getCount();

                    RootFolders rootFolders = new RootFolders();
                    if(folderName.equalsIgnoreCase("Deleted_Items")){
                        rootFolders.setFolder_name("Deleted Items");
                        rootFolders.setFolder_count(count);
                    }
                    else if(folderName.equalsIgnoreCase("Conversation_Action_Settings")){
                        rootFolders.setFolder_name("Conversation Action Settings");
                        rootFolders.setFolder_count(count);
                    }
                    else if(folderName.equalsIgnoreCase("Junk_E_mail")){
                        rootFolders.setFolder_name("Junk E-mail");
                        rootFolders.setFolder_count(count);
                    }
                    else if(folderName.equalsIgnoreCase("Quick_Step_Settings")){
                        rootFolders.setFolder_name("Quick Step Settings");
                        rootFolders.setFolder_count(count);
                    }
                    else if(folderName.equalsIgnoreCase("RSS_Feeds")){
                        rootFolders.setFolder_name("RSS Feeds");
                        rootFolders.setFolder_count(count);
                    }
                    else{
                        rootFolders.setFolder_name(folderName);
                        rootFolders.setFolder_count(count);
                    }


                    if(cursor.getString(0).equals("sqlite_sequence")
                            || cursor.getString(0).equals("android_metadata")
                            || cursor.getString(0).equals("Deleted_Email")
                            || cursor.getString(0).equals("Deleted_Folder")
                            || cursor.getString(0).equals("Copied_Emails")
                            || cursor.getString(0).equals("Copied_Folders")
                            || cursor.getString(0).equals("Moved_Email")
                            || cursor.getString(0).equals("Moved_Folders")
                            || cursor.getString(0).equals("New_Folders")
                            ){
                        //do nothing i.e dont add these tables names in list
                    }
                    else{
                        mRootFolderList.add(rootFolders);
                        //Collections.sort(mRootFolderList);      //Sorting in ascending
                        Collections.reverse(mRootFolderList);   //Sorting in descending by reversing
                    }

                }while(cursor.moveToNext());
            }

            //Collections.reverse(mRootFolderList);   //Sorting in descending by reversing
            Collections.sort(mRootFolderList);      //Sorting in ascending
        }
    }

    // method to remove list item
    protected void removeItemFromList(String folderName) {

        final String foldername = folderName;

        final AlertDialog.Builder alert = new AlertDialog.Builder(activity_rootfolders.this);

        alert.setTitle("Delete");
        alert.setMessage("Do you want to delete ' "+foldername+" ' ?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String nameOfFolder = null;

                // main code on after clicking yes
                //Deleting Selected item from Listview

                if(foldername.equalsIgnoreCase("Deleted Items")){
                    nameOfFolder = "Deleted_Items";
                    //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else if(foldername.equalsIgnoreCase("Inbox")){

                    //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else if(foldername.equalsIgnoreCase("Contacts")){

                    //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else if(foldername.equalsIgnoreCase("Calendar")){

                    //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else if(foldername.equalsIgnoreCase("Drafts")){

                    //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else if(foldername.equalsIgnoreCase("Journal")){

                    //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else if(foldername.equalsIgnoreCase("Notes")){
                     //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else if(foldername.equalsIgnoreCase("Outbox")){

                    //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else if(foldername.equalsIgnoreCase("Conversation Action Settings")){
                    nameOfFolder = "Conversation_Action_Settings";
                    //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else if(foldername.equalsIgnoreCase("Junk E-mail")){
                    nameOfFolder = "Junk_E_mail";
                    //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else if(foldername.equalsIgnoreCase("Quick Step Settings")){
                    nameOfFolder = "Quick_Step_Settings";
                    //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else if(foldername.equalsIgnoreCase("RSS Feeds")){
                    nameOfFolder = "RSS_Feeds";
                    //dbHelper.deleteRF(nameOfFolder);
                    Toast.makeText(getApplicationContext(),"You cannot Delete buit-in folders",Toast.LENGTH_LONG).show();
                }
                else{
                    dbHelper.deleteRF(foldername);
                }

                //Clears Lisview from Data
                mRootFolderList.clear();
                //Fetches data from DB and populates Listview
                fetchFromDB();
                adapter.notifyDataSetChanged();

            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.id_search:
                Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
                break;

            case R.id.id_add_folder:
                Toast.makeText(getApplicationContext(), "New Folder", Toast.LENGTH_SHORT).show();
                alertDialogCreateFolder();  //alert Dialog Method to add listview item
                break;

            default:
                //finish() is for back button
                finish();
                break;
        }
        return true;
    }

    //ALERT DIALOG  Method TO CREATE NEW FOLDER in LISTVIEW
    public void alertDialogCreateFolder(){

        // get create_folder_layout.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.create_folder_layout, null);

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);

        // set create_folder_layout.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.FolderName);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // get user input and show it on Toast
                                String inputFromUser=userInput.getText().toString();

                                if(inputFromUser.equals("")){
                                    Toast.makeText(getApplicationContext(), "Please enter folder name", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), inputFromUser+" Created", Toast.LENGTH_SHORT).show();


                                    //CREATING TABLE
                                    dbHelper.createTable(inputFromUser);

                                    //Clears Lisview from Data
                                    mRootFolderList.clear();
                                    //Fetches data from DB and populates Listview
                                    dbHelper.open();
                                    fetchFromDB();
                                    //dbHelper.close();
                                    adapter.notifyDataSetChanged();
                                }


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rootfolders, menu);
        MenuItem item = menu.findItem(R.id.id_search);
        final SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }


}
