package com.theghosttechnology.dragonide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringEscapeUtils.*;
import org.apache.commons.lang3.StringUtils.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.StringTokenizer;

import static android.os.Environment.getExternalStorageDirectory;
import static org.apache.commons.lang3.StringEscapeUtils.UNESCAPE_HTML4;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml3;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

public class MainActivityCodeHackIDE extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FILE_SELECT_CODE = 10;
    WebView web;

    String myfileuri;

    String filecontent;

    String pathtosave;

    String pathexternal;

    String finalfilepath;

    String filenametmp;

    String installchecker;

    String languageselected = "C/C++";

    String idetheme = "vibrant_ink";

    String extralanguages = "";

    String ext;

    String pathopened = "";

    Intent startservnot;

    private static final int MY_PERMISSIONS_REQUEST_WRITE = 22;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_codehack);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startservnot = new Intent(this, MyNot.class);
        startService(startservnot);


        int permissionCheck = ContextCompat.checkSelfPermission(MainActivityCodeHackIDE.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (ContextCompat.checkSelfPermission(MainActivityCodeHackIDE.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityCodeHackIDE.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {



            } else {



                ActivityCompat.requestPermissions(MainActivityCodeHackIDE.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE);


            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navopen, R.string.navclose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        JsInterface jsInterface = new JsInterface();

        web = findViewById(R.id.webide);
        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.addJavascriptInterface(jsInterface, "android");
        web.getSettings().setAppCacheEnabled(false);
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        web.loadUrl("file:///android_asset/ideindex.html");

        /**web.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && web.canGoBack()) {
                    return true;
                }

                return false;
            }

        }); **/



        web.getSettings().setAllowContentAccess(true);

        web.setWebChromeClient(new WebChromeClient() {
        });






        Bundle extras = getIntent().getExtras();

        if(extras !=null){
            extralanguages = extras.getString("LANG");

            if(extralanguages !=null) {

                mHandler.postDelayed(changelanghandler, 1000);
                Toast.makeText(getApplicationContext(), "LANGUAGE " + extralanguages, Toast.LENGTH_LONG).show();

            } else {
                extralanguages = "c_cpp";
            }

        }

    }


    private final Handler mHandler = new Handler();

    public Runnable changelanghandler = new Runnable() {
        public void run() {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ extralanguages + "\"); })();");
            mHandler.removeCallbacks(changelanghandler);

        }
};



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit?");
            builder.setMessage("Exit from IDE? Your project will not be saved!");
            builder.setIcon(R.drawable.codehack);

            String positiveText = getString(android.R.string.ok);
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(finalfilepath != null && !finalfilepath.isEmpty()) {
                                File f0 = new File(finalfilepath);
                                boolean d0 = f0.delete();
                                Log.w("TMPDELETE", "File deleted: " + d0);

                            }

                            stopService(startservnot);
                            finish();
                            finish();
                            finish();

                        }
                    });

            String negativeText = getString(android.R.string.cancel);
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();

            dialog.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_codehack, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_newfile) {

            myfileuri = null;
            filecontent = null;
            pathtosave = null;
            pathexternal = null;

            web.loadUrl("file:///android_asset/ideindex.html");

            setTitle("untitled.c");


        } else if (id == R.id.action_save) {

            if(myfileuri == null) {



                /**Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);

                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);

                i.putExtra("android.content.extra.SHOW_ADVANCED", true);

                i.putExtra(FilePickerActivity.EXTRA_START_PATH, getExternalStorageDirectory().getPath());

                startActivityForResult(i, 3); **/


                pathexternal = "/sdcard/";

                runnewsave();

                if(finalfilepath != null && !finalfilepath.isEmpty()) {
                    File f0 = new File(finalfilepath);
                    boolean d0 = f0.delete();
                    Log.w("TMPDELETE", "File deleted: " + d0);


                }


            } else {

                web.evaluateJavascript(
                        "(function() { return(editor.getValue()) })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {
                                String htmlunescaped;
                                htmlunescaped = unescapeHtml4(html).replace("\\u003C", "<").replace("\\n", "\n").replace("\\\n", "\\n").replace("\\\"", "\"").replace("\\\\", "\\").replace("\\t", "\t");

                                String htmlunescapedbytoken = htmlunescaped;
                                htmlunescapedbytoken = htmlunescapedbytoken.substring(1, htmlunescapedbytoken.length() - 1);

                                try {
                                    FileOutputStream fos = new FileOutputStream(pathtosave);
                                    fos.write(htmlunescapedbytoken.getBytes());
                                    fos.close();

                                    myfileuri = pathtosave;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                File f0 = new File(finalfilepath);
                                boolean d0 = f0.delete();
                                Log.w("TMPDELETE", "File deleted: " + d0);

                                Toast.makeText(getApplicationContext(), "File saved: " + pathtosave, Toast.LENGTH_LONG).show();

                                extralanguages = getExt(pathtosave);
                                mHandler.postDelayed(changelanghandler, 1000);

                            }
                        });

            }

            return true;

        /**} else if (id == R.id.action_save_as) {


            Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);

            i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
            i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);

            i.putExtra(FilePickerActivity.EXTRA_START_PATH, getExternalStorageDirectory().getPath());

            startActivityForResult(i, 3);

            pathexternal = "/sdcard/";
            runnewsave();

            return  true;

        } else if (id == R.id.action_compile) {


            try {

                Process process04 = Runtime.getRuntime().exec("echo -ne \'OK\'");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process04.getInputStream()));
                int read;

                char[] buffer = new char[8000];
                StringBuffer output = new StringBuffer();
                while ((read = reader.read(buffer)) > 0) {

                    output.append(buffer, 0, read);


                }

                process04.waitFor();

                reader.close();

                //installchecker = output.toString();
		String installchecker = "OK";


            } catch (IOException e) {
                e.printStackTrace();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (installchecker.equals("OK")) {


                new MaterialDialog.Builder(this)
                        .title("Args for compiler")
                        .content("Ex: {-fPIE -pie}, -i {args}...")
                        .backgroundColorRes(R.color.blackfull)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .positiveText("Compile/Run")
                        .input(R.string.cflags, R.string.fontsizeselected, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                                if (extralanguages.equals("c_cpp")) {

                                    Intent intentstart = new Intent("andrax.axterminal.RUN_SCRIPT");

                                    intentstart.addCategory(Intent.CATEGORY_DEFAULT);
                                    intentstart.putExtra("andrax.axterminal.iInitialCommand", "gcc " + input + " " + pathtosave);
                                    intentstart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    Intent intentstart = Bridge.createExecuteIntent("gcc" + input + " " + pathtosave);
                                    startActivity(intentstart);


                                } else if(extralanguages.equals("python")) {

                                    *Intent intentstart = new Intent("andrax.axterminal.RUN_SCRIPT");

                                    intentstart.addCategory(Intent.CATEGORY_DEFAULT);
                                    intentstart.putExtra("andrax.axterminal.iInitialCommand", "python3 " + input + " " + pathtosave);
                                    intentstart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    Intent intentstart = Bridge.createExecuteIntent("python3" + input + " " + pathtosave);
                                    startActivity(intentstart);

                                } else if(extralanguages.equals("javascript")) {

                                    Intent intentstart = new Intent("andrax.axterminal.RUN_SCRIPT");

                                    intentstart.addCategory(Intent.CATEGORY_DEFAULT);
                                    intentstart.putExtra("andrax.axterminal.iInitialCommand", "node " + input + " " + pathtosave);
                                    intentstart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    Intent intentstart = Bridge.createExecuteIntent("node" + input + " " + pathtosave);
                                    startActivity(intentstart);

                                } else if(extralanguages.equals("ruby")) {

                                    Intent intentstart = new Intent("andrax.axterminal.RUN_SCRIPT");

                                    intentstart.addCategory(Intent.CATEGORY_DEFAULT);
                                    intentstart.putExtra("andrax.axterminal.iInitialCommand", "ruby " + input + " " + pathtosave);
                                    intentstart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    Intent intentstart = Bridge.createExecuteIntent("ruby" + input + " " + pathtosave);
                                    startActivity(intentstart);

                                } else if(extralanguages.equals("php")) {

                                    Intent intentstart = new Intent("andrax.axterminal.RUN_SCRIPT");

                                    intentstart.addCategory(Intent.CATEGORY_DEFAULT);
                                    intentstart.putExtra("andrax.axterminal.iInitialCommand", "php " + input + " " + pathtosave);
                                    intentstart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    Intent intentstart = Bridge.createExecuteIntent("php" + input + " " + pathtosave);
                                    startActivity(intentstart);

                                } else if(extralanguages.equals("sh")) {
				   Intent intentstart = Bridge.createExecuteIntent("sh" + input + " " + pathtosave);
				   startActivity(intentstart);
				}

                            }
                        }).show();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Dragon Is Not installed!");
                builder.setMessage("The compilation environment requires Dragon Mobile Pentest installed on the system!\n\nIf you wish to install ANDRAX click \"OK\"");
                builder.setIcon(R.mipmap.codehack_al);

                String positiveText = getString(android.R.string.ok);
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
                                startActivity(intent);

                            }
                        });

                String negativeText = getString(android.R.string.cancel);
                builder.setNegativeButton(negativeText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog dialog = builder.create();

                dialog.show();

            }


            return true;




        **/} else if (id == R.id.action_exit) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exit?");
                builder.setMessage("Exit from IDE? Your project will not be saved!");
                builder.setIcon(R.drawable.codehack);

                String positiveText = getString(android.R.string.ok);
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (finalfilepath != null && !finalfilepath.isEmpty()) {
                                    File f0 = new File(finalfilepath);
                                    boolean d0 = f0.delete();
                                    Log.e("TMPDELETE", "File deleted: " + d0);

                                }

                                stopService(startservnot);
                                finish();
                                finish();
                                finish();

                            }
                        });

                String negativeText = getString(android.R.string.cancel);
                builder.setNegativeButton(negativeText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog dialog = builder.create();

                dialog.show();

                return true;

            }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_openfile) {

            /**Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);

            i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
            i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

            i.putExtra(FilePickerActivity.EXTRA_START_PATH, getExternalStorageDirectory().getPath());

            startActivityForResult(i, 5);**/


            // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
            // browser.
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Filter to show only images, using the image MIME data type.
            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            intent.setType("*/*");

            startActivityForResult(intent, 5);





        } else if (id == R.id.nav_fontsize) {

            new MaterialDialog.Builder(this)
                    .title("Change font size")
                    .content("Default: 12px")
                    .backgroundColorRes(R.color.blackfull)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .positiveText("Change")
                    .input(R.string.fontdefault, R.string.fontsizeselected, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            if(input == null){

                            } else {
                                web.loadUrl("javascript:(function() { document.getElementById('editor').style.fontSize='" + input + "px" + "' })();");
                            }
                        }
                    }).show();

        } else if(id == R.id.nav_language) {

            new MaterialDialog.Builder(this)
                    .title("Language")
                    .items(R.array.itemlang)
                    .backgroundColorRes(R.color.blackfull)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                            languageselected = text.toString();

                            if(text.equals("C/C++")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "c_cpp" + "\"); })();");
                                extralanguages = "c_cpp";
                            } else if(text.equals("ASM")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "assembly_x86" + "\"); })();");
                                extralanguages = "assembly_x86";
                            } else if(text.equals("Makefile")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "makefile" + "\"); })();");
                                extralanguages = "makefile";
                            } else if(text.equals("Python")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "python" + "\"); })();");
                                extralanguages = "python";
                            } else if(text.equals("Perl")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "perl" + "\"); })();");
                                extralanguages = "perl";
                            } else if(text.equals("Lua")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "lua" + "\"); })();");
                                extralanguages = "lua";
                            } else if(text.equals("Shell")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "sh" + "\"); })();");
                                extralanguages = "sh";
                            } else if(text.equals("R")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "r" + "\"); })();");
                                extralanguages = "r";
                            } else if(text.equals("Haskell")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "haskell" + "\"); })();");
                                extralanguages = "haskell";
                            } else if(text.equals("Java")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "java" + "\"); })();");
                                extralanguages = "java";
                            } else if(text.equals("Go")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "golang" + "\"); })();");
                                extralanguages = "golang";
                            } else if(text.equals("PHP")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "php" + "\"); })();");
                                extralanguages = "php";
                            } else if(text.equals("Html")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "html" + "\"); })();");
                                extralanguages = "html";
                            } else if(text.equals("CSS")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "css" + "\"); })();");
                                extralanguages = "css";
                            } else if(text.equals("Javascript")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "javascript" + "\"); })();");
                                extralanguages = "javascript";
                            } else if(text.equals("Ruby")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "ruby" + "\"); })();");
                                extralanguages = "ruby";
                            } else if(text.equals("C#")) {
                                web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "csharp" + "\"); })();");
                                extralanguages = "csharp";
                            }
                            return true;
                        }
                    })
                    //.positiveText("Select")
                    //  HACK to BYPASS CRASH
                    .negativeText("Select")
                    .show();

        } else if(id == R.id.nav_theme) {

            new MaterialDialog.Builder(this)
                    .title("Theme")
                    .backgroundColorRes(R.color.blackfull)
                    .items(R.array.itemtheme)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                            idetheme = text.toString();

                            web.loadUrl("javascript:(function() { editor.setTheme(\"ace/theme/" + idetheme + "\"); })();");

                            return true;
                        }
                    })
                    //.positiveText("Select")
                    .negativeText("Select")
                    .show();

        } else if(id == R.id.nav_about) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About");
            builder.setMessage("Dragon IDE, is the official IDE of the Dragon Mobile Pentest DISTRO\n\n" +
                    "Developed by The Ghost Technology as part of the Android Hacking project a standardization of computer security from smartphones\n\n" +
                    "Dev: Rabby Sheikh\n\thttp://github.com/xploitednoob");
            builder.setIcon(R.drawable.codehack);

            String positiveText = getString(android.R.string.ok);
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

           /** String negativeText = getString(android.R.string.cancel);
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }); **/

            AlertDialog dialog = builder.create();

            dialog.show();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { //checking
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    private void webViewGoBack(){
        web.goBack();
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            Log.e("PAGESTART", "Started");
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e("PAGEFIM", "Finalizado");
            super.onPageFinished(view, url);
            web.loadUrl("javascript:(function() { editor.setTheme(\"ace/theme/" + idetheme + "\")})();");

           // webView.loadUrl("javascript:(function() { editor.setTheme(\"ace/theme/vibrant_ink\"); })()");


        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);
            web.loadUrl("file:///android_asset/error.html");
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();
                    Log.d("FileCHOOSED", "File Uri: " + uri);

                    String path;
                    assert uri != null;
                    path = uri.getPath();
                    Log.d("FileCHOOSED", "File Path: " + path);

                    try {
                        myfileuri = getFilePath(getApplicationContext(), uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    setcodeoffile();
                }
                break;

            case 3:

                if(resultCode == Activity.RESULT_OK){

                    List<Uri> files = Utils.getSelectedFilesFromResult(data);

                    for (Uri uri: files) {
                        //File file = Utils.getFileForUri(uri);

                        try {
                            pathexternal = getFilePath(getApplicationContext(), uri);
                            pathtosave = getFilePath(getApplicationContext(), uri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        extralanguages = getExt(pathtosave);
                        mHandler.postDelayed(changelanghandler, 1000);
                        runnewsave();

                    }

                }

                break;


            case 5:

                if(resultCode == Activity.RESULT_OK){

                    List<Uri> files = Utils.getSelectedFilesFromResult(data);

                    for (Uri uri: files) {

                        try {
                            myfileuri = getFilePath(getApplicationContext(), uri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        setcodeoffile();

                    }


                }

                break;




        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    stopService(startservnot);
                    finish();
                    finish();
                    finish();

                }
            }


        }
    }


    public void setcodeoffile() {

        String line = null;

        try {
            FileInputStream fileInputStream = new FileInputStream (new File(myfileuri));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line + "\n");
            }
            fileInputStream.close();
            line = stringBuilder.toString();
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            Log.d("FILECHOOSED", ex.getMessage());
        }
        catch(IOException ex) {
            Log.d("FILECHOOSED", ex.getMessage());
        }

        filecontent = line;


        writetempfile(filecontent, getApplicationContext());


        //web.loadUrl(javascript);


    }

    public void writetempfile(String dataoftext, Context context) {

        int i = myfileuri.lastIndexOf('/');
        String originalfilename = myfileuri.substring(i+1);
        String originalpath = myfileuri.substring(0, myfileuri.lastIndexOf('/')+1);

        String datatoview = dataoftext.trim();

        finalfilepath = getFilesDir().getAbsolutePath() + "/" + "." + originalfilename + ".tmp";

        Toast.makeText(getApplicationContext(), "PATH " + finalfilepath, Toast.LENGTH_LONG).show();

        pathtosave = originalpath + originalfilename;

        pathopened = originalpath;

        ext = getExt(originalfilename);

        extralanguages = ext;
        mHandler.postDelayed(changelanghandler, 1000);

        setTitle(originalfilename);

        String htmltoplace = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>ACE Autocompletion demo</title>\n" +
                "  <style type=\"text/css\" media=\"screen\">\n" +
                "    body {\n" +
                "        overflow: hidden;\n" +
                "    }\n" +
                "    \n" +
                "    #editor { \n" +
                "        margin: 0;\n" +
                "        position: absolute;\n" +
                "        top: 0;\n" +
                "        bottom: 0;\n" +
                "        left: 0;\n" +
                "        right: 0;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<pre id=\"editor\">"+ escapeHtml4(datatoview).replace("\\\\", "\\") +
                "</pre>\n" +
                "\n" +
                "<!-- load ace -->\n" +
                "<script src=\"file:///android_asset/src-noconflict/ace.js\"></script>\n" +
                "<!-- load ace language tools -->\n" +
                "<script src=\"file:///android_asset/src-noconflict/ext-language_tools.js\"></script>\n" +
                "<script>\n" +
                "    // trigger extension\n" +
                "    ace.require(\"ace/ext/language_tools\");\n" +
                "    var editor = ace.edit(\"editor\");\n" +
                "    editor.session.setMode(\"ace/mode/" + ext + "\");\n" +
                "    editor.setShowPrintMargin(false);\n" +
                "    editor.getSession().setUseWrapMode(true);\n" +
                "    var code = editor.getValue();\n" +
                "    //editor.setValue(\"new code \" + code);\n" +
                "    //enable autocompletion and snippets\n" +
                "    editor.setOptions({\n" +
                "        enableBasicAutocompletion: true,\n" +
                "        enableSnippets: true,\n" +
                "        enableLiveAutocompletion: true\n" +
                "    });\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";

        //Toast.makeText(getApplicationContext(), "PATH " + originalpath, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "Filename " + originalfilename, Toast.LENGTH_LONG).show();

       /** try {
            FileOutputStream fos = new FileOutputStream(finalfilepath);
            //fos.write(dataoftext.replace("\\n", "\n").trim().getBytes());
            fos.write(htmltoplace.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } **/

        try {
            FileWriter writer = new FileWriter(finalfilepath);
            writer.write(htmltoplace);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }






        readsavedtmpfile(finalfilepath);


    }

    public void readsavedtmpfile(String pathpassed) {

        web.loadUrl("file://" + pathpassed);

        /**if(ext.equals("txt")) {

        } else if(ext.equals("c") || ext.equals("cpp") || ext.equals("h") || ext.equals("cc")) {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "c_cpp" + "\"); })();");
        } else if(ext.equals("py")) {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "python" + "\"); })();");
        } else if(ext.equals("asm")) {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "assembly_x86" + "\"); })();");
        } else if(ext.equals("js")) {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "javascript" + "\"); })();");
        } else if(ext.equals("mk")) {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "makefile" + "\"); })();");
        } else if(ext.equals("php")) {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "php" + "\"); })();");
        } else if(ext.equals("html")) {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "html" + "\"); })();");
        } else if(ext.equals("go")) {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "golang" + "\"); })();");
        } else if(ext.equals("java")) {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "java" + "\"); })();");
        } else if(ext.equals("java")) {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "css" + "\"); })();");
        } else {
            web.loadUrl("javascript:(function() { editor.getSession().setMode(\"ace/mode/"+ "c_cpp" + "\"); })();");
        }

        Toast.makeText(getApplicationContext(), "New file saved: " + ext, Toast.LENGTH_LONG).show();
        **/

    }


    public String getExt(String filePath){
        int strLength = filePath.lastIndexOf(".");
        if(strLength > 0)

            if(filePath.substring(strLength + 1).toLowerCase().equals("txt")) {

            } else if(filePath.substring(strLength + 1).toLowerCase().equals("c") || filePath.substring(strLength + 1).toLowerCase().equals("cpp") || filePath.substring(strLength + 1).toLowerCase().equals("h") || filePath.substring(strLength + 1).toLowerCase().equals("cc")) {
                return "c_cpp";
            } else if(filePath.substring(strLength + 1).toLowerCase().equals("py")) {
                return "python";
            } else if(filePath.substring(strLength + 1).toLowerCase().equals("asm")) {
                return "assembly_x86";
            } else if(filePath.substring(strLength + 1).toLowerCase().equals("rb")) {
                return "ruby";
            } else if(filePath.substring(strLength + 1).toLowerCase().equals("js")) {
                return "javascript";
            } else if(filePath.substring(strLength + 1).toLowerCase().equals("mk")) {
                return "makefile";
            } else if(filePath.substring(strLength + 1).toLowerCase().equals("php")) {
                return "php";
            } else if(filePath.substring(strLength + 1).toLowerCase().equals("html")) {
                return "html";
            } else if(filePath.substring(strLength + 1).toLowerCase().equals("go")) {
                return "golang";
            } else if(filePath.substring(strLength + 1).toLowerCase().equals("java")) {
                return "java";
            } else if(filePath.substring(strLength + 1).toLowerCase().equals("css")) {
                return "css";
            } else {
                return "c_cpp";
            }
        return null;
    }


    public void runnewsave() {

        if(pathexternal != null) {

            new MaterialDialog.Builder(this)
                    .title("New file name")
                    .content("The file will be saved in: " + pathexternal)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .positiveText("Save")
                    .contentColor(Color.WHITE)
                    .backgroundColorRes(R.color.blackfull)
                    .input(R.string.newfilename, R.string.fontsizeselected, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, final CharSequence input) {

                            web.evaluateJavascript(
                                    "(function() { return(editor.getValue()) })();",
                                    new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String html) {
                                            String htmlunescaped;
                                            htmlunescaped = unescapeHtml4(html).replace("\\u003C", "<").replace("\\n", "\n").replace("\\\n", "\\n").replace("\\\"", "\"").replace("\\\\", "\\").replace("\\t", "\t");

                                            String htmlunescapedbytoken = htmlunescaped;
                                            htmlunescapedbytoken = htmlunescapedbytoken.substring(1, htmlunescapedbytoken.length() - 1);

                                            try {
                                                FileOutputStream fos = new FileOutputStream(pathexternal + "/" + input);

                                                fos.write(htmlunescapedbytoken.getBytes());
                                                fos.close();

                                                pathtosave = pathexternal + "/" + input;
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            setTitle(input);

                                            Toast.makeText(getApplicationContext(), "New file saved: " + input, Toast.LENGTH_LONG).show();
                                            extralanguages = getExt(pathtosave);
                                            mHandler.postDelayed(changelanghandler, 1000);

                                        }
                                    });

                        }
                    }).show();


        } else {
            Toast.makeText(getApplicationContext(), "Not saved!", Toast.LENGTH_LONG).show();
        }

    }


    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;

        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    private class JsInterface{
        
        public void log(String msg){


        }
    }








}
