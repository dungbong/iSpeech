package com.example.dungbong.finalproject;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendMailFragment extends Fragment {


    public SendMailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_mail, container, false);

        ImageButton button = view.findViewById(R.id.send_mail);

        TextView text_to = view.findViewById(R.id.send_to);
        text_to.setText(ContactFragment.str);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text_who = getView().findViewById(R.id.send_to);
                String string_who = text_who.getText().toString();

                EditText text_subject = getView().findViewById(R.id.subject);
                String string_subject = text_subject.getText().toString();

                EditText text_content = getView().findViewById(R.id.content);
                String string_content = text_content.getText().toString();

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                //emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ContactFragment.str});
                //emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, string_subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, string_content);

                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            }
        });



        return view;
    }

}
