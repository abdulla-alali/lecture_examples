package qa.edu.qu.cse.cmps497.listview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import qa.edu.qu.cse.cmps497.listview.adapter.ListAdapter;
import qa.edu.qu.cse.cmps497.listview.model.Item;

public class MainActivity extends AppCompatActivity {

    ListView mListView;

    public final static String LABEL = "label";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get an instance of the list view
        mListView = (ListView) findViewById(R.id.list);

        //get the string array resource containing the items
        final String[] adobeProducts = getResources().getStringArray(R.array.adobe_products);

        mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.listview_row_item, R.id.label, adobeProducts));

        //Advance: custom rows via a custom adapter
        //If you uncomment this block, comment out the setAdapter line above (line 31)
        /*ArrayList<Item> myItems = new ArrayList<Item>();
        for (int i=0; i<adobeProducts.length; i++) {
            myItems.add(new Item(i+1, adobeProducts[i]));
        }
        mListView.setAdapter(new ListAdapter(this, R.layout.listview_row_item_custom, myItems));*/

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //getApplicationContext() is equivalent to Text
                Intent intent = new Intent(getApplicationContext(), ListItemDetails.class);
                intent.putExtra(LABEL, adobeProducts[position]);
                startActivity(intent);
            }
        });

    }
}
