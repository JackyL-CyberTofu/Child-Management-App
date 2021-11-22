package ca.sfu.cmpt276.be.parentapp.view;

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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.DataManager;
import ca.sfu.cmpt276.be.parentapp.controller.ChildManager;
import ca.sfu.cmpt276.be.parentapp.controller.ImageManager;
import ca.sfu.cmpt276.be.parentapp.model.Coin;
import ca.sfu.cmpt276.be.parentapp.controller.CoinFlipManager;

/**
Diplays a list of previous coin flip results
 Shows if the person that picked won, with the time and child name.
 */

public class FlipHistoryActivity extends AppCompatActivity {

    ArrayList<Coin> coinFlipHistory = DataManager.getInstance().getCoinFlipHistory();
    ChildManager childManager = new ChildManager();
    CoinFlipManager coinFlipManager = new CoinFlipManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_history);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



        ArrayAdapter<Coin> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.game_list);
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
            super(FlipHistoryActivity.this, R.layout.layout_coinflip, coinFlipHistory);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.layout_coinflip, parent, false);
            }

            ImageView image_child = (ImageView) itemView.findViewById(R.id.image_layout_child);
            ImageManager imageManager = new ImageManager();
            image_child.setImageBitmap(imageManager.getPortrait(FlipHistoryActivity.this, coinFlipManager.getCoinFlipID(position)));

            String string_result = coinFlipManager.getCoinFlipGame(position).getResult();
            String string_time = coinFlipManager.getCoinFlipGame(position).getDate();

            TextView text_upper = (TextView) itemView.findViewById(R.id.text_upper);
            text_upper.setText(MessageFormat.format("{0}{1}{2}", string_result, getString(R.string.AT), string_time));

            TextView text_lower = (TextView) itemView.findViewById(R.id.text_lower);
            if (coinFlipManager.getCoinFlipGame(position).getPicker()==null){
                text_lower.setText(R.string.text_noChildrenSelected);
            }
            else {
                text_lower.setText(MessageFormat.format("{0}{1}", getString(R.string.text_pickedBy), coinFlipManager.getCoinFlipGame(position).getPicker()));
            }

            ImageView image_result = (ImageView) itemView.findViewById(R.id.image_result);
            if (coinFlipManager.getCoinFlipGame(position).getPickerWon()==1){
                image_result.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            }
            else {
                image_result.setImageResource(R.drawable.ic_baseline_highlight_off_24);
            }
            if (coinFlipManager.getCoinFlipGame(position).getPicker()==null){
                image_result.setImageResource(R.drawable.ic_baseline_help_outline_24);
            }
            //Fill the view
            return itemView;
        }
    }
}