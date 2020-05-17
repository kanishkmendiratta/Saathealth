package com.example.saathealth;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


public class SelectLangDialog extends AppCompatDialogFragment {
    private RadioGroup langRadio;
    private langSelectListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.select_language, null);

        builder.setView(view)
                .setTitle("Select Language\nकृपया भाषा चुने")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id =langRadio.getCheckedRadioButtonId();
                        if(id==R.id.eng){
                            listener.applyTexts("English");
                        }
                        else if(id==R.id.hind){
                            listener.applyTexts("Hindi");
                        }
                    }
                });

        langRadio=view.findViewById(R.id.langRadio);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (langSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement langSelectListener");
        }
    }

    public interface langSelectListener {
        void applyTexts(String lang);
    }
}

