package ca.sfu.cmpt276.be.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.MessageFormat;

import ca.sfu.cmpt276.be.parentapp.model.Child;
import ca.sfu.cmpt276.be.parentapp.model.Coin;
import ca.sfu.cmpt276.be.parentapp.model.CoinFlipManager;

public class FlipQueueActivity extends AppCompatActivity {

    CoinFlipManager coinFlipManager = CoinFlipManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_queue);

        ArrayAdapter<Child> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.flip_list);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter() {
            super(FlipQueueActivity.this, R.layout.item_view, coinFlipManager.getCoinFlipQueue()) ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            TextView upper = (TextView) itemView.findViewById(R.id.textView1);
            upper.setText(coinFlipManager.getCoinFlipQueue().get(position).getName());

            //Fill the view
            return itemView;
        }
    }
}