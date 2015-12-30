package com.jiaotong.aiton.myapplication.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import com.jiaotong.aiton.myapplication.R;

/**
 * Created by zjb on 2015/12/29.
 */
public class MyDialog extends View{
    Context mContext;
    public MyDialog(Context context) {
        super(context);
        mContext=context;
    }

    public void setDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.setView(view)
                .create();
        dialog.setCancelable(false);
        dialog.show();
        view.findViewById(R.id.ISee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
