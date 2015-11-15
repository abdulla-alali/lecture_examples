package qa.edu.qu.cse.cmps497.listview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import qa.edu.qu.cse.cmps497.listview.R;
import qa.edu.qu.cse.cmps497.listview.model.Item;

public class ListAdapter extends ArrayAdapter<Item> {


    /**
     * SuperClass method that you must overwrite. This method is called
     * in setAdapter on the ListView object
     * @param resource is the resource layout ID of your custom row.
     * @param items Are the list of items in a List format (Model)
     */
    public ListAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
    }

    /**
     * Must overwrite this method. Return a view of each row
     * @param position the position of the item you are drawing
     * @param convertView the view you are inflating into
     * @param parent parent view (if any)
     * @return return the row item view to display in the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            Log.i("TAG", "View is null");
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_row_item_custom, parent, false);
        }

        //getItem() is a super class method that retrieves the item that corresponds to this row
        //of this listView
        Item p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.number);
            TextView tt2 = (TextView) v.findViewById(R.id.label);


            tt1.setText(p.getNumber() + "");


            tt2.setText(p.getName());

        }

        return v;
    }

}