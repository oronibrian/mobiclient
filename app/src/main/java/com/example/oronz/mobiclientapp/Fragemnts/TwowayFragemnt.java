package com.example.oronz.mobiclientapp.Fragemnts;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.oronz.mobiclientapp.R;

import java.util.Calendar;


public class TwowayFragemnt extends Fragment {
    Button search2;
    static EditText txttrvel_retun,txttrvel_date;
    EditText no_of_pass;
    DatePickerDialog picker;
    public TwowayFragemnt() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V =inflater.inflate(R.layout.fragment_twoway_fragemnt, container, false);


        return V;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        search2 =view.findViewById(R.id.search2);
        search2.setOnClickListener(v -> {

        });


        txttrvel_retun=view.findViewById(R.id.txttrvel_retun);
        txttrvel_retun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment2();
                newFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });


        txttrvel_date=view.findViewById(R.id.txttrvel_date);
        txttrvel_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment1();
                newFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        no_of_pass = view.findViewById(R.id.num_of_passengers2);



    }


    public static class DatePickerFragment1 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date_selected = (day ) + "/" + month + "/"
                    + year;

            txttrvel_date.setText(date_selected);
        }
    }

    public static class DatePickerFragment2 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date_selected = (day ) + "/" + month + "/"
                    + year;

            txttrvel_retun.setText(date_selected);
        }
    }


}
