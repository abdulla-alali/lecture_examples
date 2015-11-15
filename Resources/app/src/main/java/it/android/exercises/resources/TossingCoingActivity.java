package it.android.exercises.resources;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class TossingCoingActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private Button buttonToss;
	private Button buttonRepeat;
	private Button buttonExit;
	private Random rand;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rand=new Random();
        setContentView(R.layout.main);
        buttonToss=(Button)findViewById(R.id.button1);
        buttonRepeat=(Button)findViewById(R.id.button2);
        buttonExit=(Button)findViewById(R.id.button3);
        buttonToss.setOnClickListener(this);
        buttonExit.setOnClickListener(this);
        buttonRepeat.setOnClickListener(this);
    }

    
    
	@Override
	public void onClick(View view) {
		
		if (view==buttonToss) {
		
			// TODO Auto-generated method stub
			Log.d("CLICK_EVENT", "Toss button clicked! ");
			TextView tw=(TextView)findViewById(R.id.textView1);
			ImageView iw=(ImageView)findViewById(R.id.imageView1);
		
			if (rand.nextDouble() <0.5) {
				tw.setText(getResources().getString(R.string.val1));
				iw.setImageResource(R.drawable.head);
			}
			else { 
				tw.setText(getResources().getString(R.string.val2));
				iw.setImageResource(R.drawable.tail);
			}
		
			buttonRepeat.setVisibility(View.VISIBLE);
			buttonExit.setVisibility(View.VISIBLE);
			buttonToss.setVisibility(View.INVISIBLE);
		}
		
		else if (view==buttonRepeat) {
			
			TextView tw=(TextView)findViewById(R.id.textView1);
			ImageView iw=(ImageView)findViewById(R.id.imageView1);
			
			buttonRepeat.setVisibility(View.INVISIBLE);
			buttonExit.setVisibility(View.INVISIBLE);
			buttonToss.setVisibility(View.VISIBLE);
			tw.setText("");
			iw.setImageResource(R.drawable.questionpic);
		
		} else if (view==buttonExit) {
			
			this.finish();
			
		}
    
	}	

}