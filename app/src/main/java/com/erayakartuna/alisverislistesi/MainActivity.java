package com.erayakartuna.alisverislistesi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Database db;
    ImageButton add_list;
    EditText shop_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onResume()
    {
        super.onResume();

        db = new Database(getApplicationContext());

        updateList(db);
        add_list = (ImageButton) findViewById(R.id.shop_title_button);

        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText shop_text = (EditText) findViewById(R.id.shop_title_text);
                db.addList(shop_text.getText().toString());
                shop_text.setText("");
                updateList(db);
            }
        });
    }

    public  void updateList(Database db)
    {
        set_adapter(db.lists());
    }

    private void set_adapter(ArrayList<HashMap<String, String>> items) {
        ListView listemiz = (ListView) findViewById(R.id.listview);
        listemiz.setAdapter(new MainActivity.CustomListAdapter(this, items));
    }

    private class CustomListAdapter extends BaseAdapter {

        Context context;
        public ArrayList<HashMap<String, String>> list;

        private LayoutInflater inflater = null;

        public CustomListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.list = list;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi = convertView;

            if (vi == null)
                vi = inflater.inflate(R.layout.show_list, null);


            TextView title = (TextView) vi.findViewById(R.id.list_title);
            title.setText(list.get(position).get("title").toString());

            ImageButton remove = (ImageButton) vi.findViewById(R.id.remove);
            ImageButton preview = (ImageButton) vi.findViewById(R.id.preview);


            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this,ItemsActivity.class);
                    i.putExtra("title",list.get(position).get("title").toString());
                    i.putExtra("id",list.get(position).get("shop_id").toString());
                    startActivity(i);
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.sure_to_delete_title)
                            .setMessage(R.string.sure_to_delete_list)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    db.removeList(Integer.parseInt(list.get(position).get("shop_id")));
                                    Toast.makeText(MainActivity.this,R.string.deleted_message, Toast.LENGTH_LONG);
                                    updateList(db);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });

            return vi;
        }

    }


}
