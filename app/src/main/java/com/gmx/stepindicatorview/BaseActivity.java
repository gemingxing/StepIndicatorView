package com.gmx.stepindicatorview;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.horizontal:
                if (!getClass().getSimpleName().equals("MainActivity")) {
                    setUpHorizontal();
                }
                break;
            case R.id.vertical:
                if (!getClass().getSimpleName().equals("VerticalActivity")) {
                    setUpVertical();
                }
                break;
            case R.id.without:
                if (!getClass().getSimpleName().equals("WithoutTextActivity")) {
                    setUpWithoutText();
                }
                break;
        }
        return true;
    }


    private void setUpWithoutText() {
        Intent intent= new Intent(this, WithoutTextActivity.class);
        ActivityCompat.startActivity(this, intent, null);
    }

    private void setUpVertical() {
        Intent intent= new Intent(this, VerticalActivity.class);
        ActivityCompat.startActivity(this, intent, null);
    }
    private void setUpHorizontal() {
        Intent intent= new Intent(this, MainActivity.class);
        ActivityCompat.startActivity(this, intent, null);
    }

}
