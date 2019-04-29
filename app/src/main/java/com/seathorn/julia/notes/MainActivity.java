package com.seathorn.julia.notes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialisation of add button
        FloatingActionButton add = findViewById(R.id.fab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNote = new Intent(MainActivity.this, NoteDetailsActivity.class);
                MainActivity.this.startActivity(addNote);
            }
        });

        realm = Realm.getDefaultInstance();

        RealmResults<RealmModel> items = realm.where(RealmModel.class).findAll();
        items = items.sort("timestamp", Sort.DESCENDING);
        final RealmAdapter adapter = new RealmAdapter(this, items);

        ListView listView = (ListView) findViewById(R.id.notes_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final RealmModel item = (RealmModel) adapterView.getAdapter().getItem(i);
                Intent changeNote = new Intent(MainActivity.this, NoteDetailsActivity.class);
                changeNote.putExtra("item_ID", item.getId());
                MainActivity.this.startActivity(changeNote);
            }
        });

    }


}
