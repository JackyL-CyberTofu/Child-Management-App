package ca.sfu.cmpt276.be.parentapp.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import ca.sfu.cmpt276.be.parentapp.R;
import ca.sfu.cmpt276.be.parentapp.controller.DataManager;

public class HistoryFragment extends AppCompatDialogFragment {

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View dialog = LayoutInflater.from(getActivity())
                .inflate(R.layout.breath_history_layout, null);

        int breaths = DataManager.getInstance().getTotalBreaths();
        int seconds = ((BreathActivity.EXHALE_TIME + BreathActivity.INHALE_TIME) * breaths)/1000;

        TextView totalBreathText = dialog.findViewById(R.id.text_breath_total);
        String totalBreath = dialog.getResources().getString(R.string.text_breath_history_count);
        if (breaths == 1) {
            totalBreath = dialog.getResources().getString(R.string.text_breath_history_count_single);
        }
        String totalBreathWithTotal = String.format(totalBreath, breaths);
        totalBreathText.setText(totalBreathWithTotal);

        TextView breathFlavourText = dialog.findViewById(R.id.text_breath_total_flavour);
        String flavourFormat = dialog.getResources().getString(R.string.text_breath_history_flavour);
        String totalWithFlavour = String.format(flavourFormat, seconds);
        breathFlavourText.setText(totalWithFlavour);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_breath_history_frag)
                .setView(dialog)
                .setPositiveButton(R.string.button_positive_breath_history, null)
                .create();
    }
}
