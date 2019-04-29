package com.seathorn.julia.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import java.util.UUID;
import io.realm.Realm;

public class NoteDetailsActivity extends AppCompatActivity {
    private Realm realm;
    private MainActivity activity;
    EditText itemText;
    String item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        itemText = (EditText) findViewById(R.id.itemText);
        item_id = getIntent().getStringExtra("item_ID");

        if (item_id != null && !item_id.isEmpty()) {
            RealmModel item = realm.where(RealmModel.class).equalTo("id", item_id).findFirst();
            itemText.setText(item.getName());
        }
    }

    @Override
    public void onBackPressed(){
        if (item_id != null && !item_id.isEmpty()){
            if (!String.valueOf(itemText.getText()).isEmpty()) {
                changeItemName(item_id, String.valueOf(itemText.getText()));
            }
            Log.d("REALM", "Change name");
        } else {
            if (!String.valueOf(itemText.getText()).isEmpty()){
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmModel item = realm.createObject(RealmModel.class, UUID.randomUUID().toString());
                        item.setTimestamp(System.currentTimeMillis());
                        item.setName(String.valueOf(itemText.getText()));
                    }
                });
                Log.d("REALM", "Create object");
            }
        }
        super.onBackPressed();
    }

    private void changeItemName(final String itemId, final String name) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmModel item = realm.where(RealmModel.class).equalTo("id", itemId).findFirst();
                item.setName(name);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_delete:
                // delete item
                if (item_id != null && !item_id.isEmpty()){
                    deleteItem(item_id);
                    Log.d("REALM", "Delete item");
                }
                super.finish();
                return true;
            case R.id.action_share:
                // share item
                if (!String.valueOf(itemText.getText()).isEmpty()) {
                    shareItem(String.valueOf(itemText.getText()));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareItem(String text){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = text;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My note");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void deleteItem(final String item_id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(RealmModel.class).equalTo("id", item_id)
                        .findFirst()
                        .deleteFromRealm();
            }
        });
    }
}
