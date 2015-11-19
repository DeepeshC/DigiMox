package com.digimox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

public class DMLoginActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dm_login);
        setOnClickListener();

    }

    private void setOnClickListener() {
        findViewById(R.id.language_select).setOnClickListener(this);
        findViewById(R.id.sign_in).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.language_select:
                showLanguagePopUp();
                break;
            case R.id.sign_in:
                signIn();
                break;
            default:
                break;
        }
    }

    private void signIn() {
        startActivity(new Intent(this, DMHomeActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_language, menu);
        return true;
    }

    private void showLanguagePopUp() {
        PopupMenu pum = new PopupMenu(this, findViewById(R.id.language_select));
        pum.inflate(R.menu.menu_language);
        pum.show();
        pum.setOnMenuItemClickListener(listener);
    }

    public PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.english:
                    selectEnglish();
                    break;
                case R.id.arabic:
                    selectArabic();
                    break;
            }

            return true;
        }
    };

    private void setLanguage(String language, String languageName) {
        ((TextView) findViewById(R.id.language_select)).setText(languageName);
    }


    public void selectEnglish() {
        setLanguage("", getResources().getString(R.string.english));
    }

    public void selectArabic() {
        setLanguage("", getResources().getString(R.string.arabic));
    }
}
