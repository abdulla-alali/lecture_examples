package qa.edu.qu.cse.cmps497.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ListItemDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_details);

        String label = getIntent().getExtras().getString(MainActivity.LABEL);

        TextView textView = (TextView)findViewById(R.id.product_label);
        textView.setText(label);

    }
}
