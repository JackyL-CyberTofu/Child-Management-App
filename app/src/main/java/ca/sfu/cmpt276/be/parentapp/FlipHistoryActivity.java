package ca.sfu.cmpt276.be.parentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.model.Coin;
import ca.sfu.cmpt276.be.parentapp.model.CoinFlipManager;

public class FlipHistoryActivity extends AppCompatActivity {

    CoinFlipManager coinFlipManager = CoinFlipManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_history);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<Coin> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.gamelist);
        list.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyListAdapter extends ArrayAdapter<Coin> {
        public MyListAdapter() {
            super(FlipHistoryActivity.this, R.layout.item_view, coinFlipManager.getCoinList());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView1);


            TextView upper = (TextView) itemView.findViewById(R.id.textView1);
            upper.setText(coinFlipManager.getCoinFlipGame(position).getDate());

            TextView under = (TextView) itemView.findViewById(R.id.textView2);
            under.setText(coinFlipManager.getCoinFlipGame(position).getResult());

            //Fill the view
            return itemView;
        }
    }
}