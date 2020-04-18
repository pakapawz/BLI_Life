package components.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.activities.R;

public class CustomProgressDialog {

    private Activity activity;
    private AlertDialog dialog;

    public CustomProgressDialog(Activity activity){
        this.activity = activity;
    }

    public void startDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_progress_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void stopDialog(){
        dialog.dismiss();
    }
}
