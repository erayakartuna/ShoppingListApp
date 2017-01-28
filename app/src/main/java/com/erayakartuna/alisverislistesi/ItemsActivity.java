package com.erayakartuna.alisverislistesi;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemsActivity extends AppCompatActivity {

    public static int list_id;
    Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Bundle extras = getIntent().getExtras();
        String title = extras.getString("title");
        list_id = Integer.parseInt(extras.getString("id"));

        TextView list_title = (TextView) findViewById(R.id.list_title);
        list_title.setText(title);

        ImageButton add_list = (ImageButton) findViewById(R.id.shop_title_button);

        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText shop_text = (EditText) findViewById(R.id.shop_title_text);
                db.addItem(shop_text.getText().toString(),list_id);
                shop_text.setText("");
                updateList(db);
            }
        });
    }

    public void onResume()
    {
        super.onResume();
        db = new Database(getApplicationContext());
        updateList(db);
    }

    /**
     * Trigger to list view update
     *
     * @param db | Database
     */
    public  void updateList(Database db)
    {
        set_adapter(db.items(list_id));
    }

    /**
     * Update to item list view
     *
     * @param items
     */

    private void set_adapter(ArrayList<HashMap<String, String>> items) {
        ListView item_list = (ListView) findViewById(R.id.listview);
        item_list.setAdapter(new ItemsActivity.CustomListAdapter(this, items));
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
                vi = inflater.inflate(R.layout.item_list, null);

            CheckBox checkbox = (CheckBox) vi.findViewById(R.id.checkbox);
            ImageButton remove = (ImageButton) vi.findViewById(R.id.remove);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(ItemsActivity.this)
                            .setTitle(R.string.sure_to_delete_title)
                            .setMessage(R.string.sure_to_delete_list)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    db.removeItem(Integer.parseInt(list.get(position).get("item_id")));
                                    Toast.makeText(ItemsActivity.this,R.string.deleted_message, Toast.LENGTH_LONG);
                                    updateList(db);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });
            
            if(list.get(position).get("checked").toString().equals("1"))
            {
                checkbox.setChecked(true);
            }

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if ( compoundButton.isChecked() )
                    {
                        db.updateItem(Integer.parseInt(list.get(position).get("item_id")),1);
                    }
                    else{
                        db.updateItem(Integer.parseInt(list.get(position).get("item_id")),0);
                    }
                }
            });

            TextView title = (TextView) vi.findViewById(R.id.list_title);
            title.setText(list.get(position).get("title").toString());

            return vi;
        }

    }

    /**
     * Finish Intent
     * @param v
     */
    public void goBack(View v)
    {
        finish();
    }
}
