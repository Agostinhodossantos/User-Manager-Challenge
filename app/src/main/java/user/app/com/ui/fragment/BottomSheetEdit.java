package user.app.com.ui.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import user.app.com.R;
import user.app.com.models.User;
import user.app.com.utils.Constants;
import user.app.com.utils.StringUtils;

public class BottomSheetEdit extends BottomSheetDialogFragment  {
    View view;
    private BottomSheetListener mListener;
    private TextView tv_title;
    private EditText ed_text;
    private MaterialButton btn_save , btn_cancel;
    private String text;
    private int type;

    public BottomSheetEdit(String text, int type) {
        this.text = text;
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_edit_name, container , false);
        initUI();

        ed_text.setText(text);
        if (type == Constants.NAME) {
            tv_title.setText(R.string.enter_name);
            ed_text.setHint(R.string.hint_name);

        } else {
            tv_title.setText(R.string.enter_bio);
            ed_text.setHint(R.string.hint_bio);
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text  = ed_text.getText().toString().trim();

                if (StringUtils.isEmpty(text)){
                    ed_text.setHintTextColor(Color.RED);
                }else {
                    mListener.onSaveButtonClicked(text, type);
                    dismiss();
                }

            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return  view;
    }


    private void initUI() {
        btn_save   = view.findViewById(R.id.btn_save);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        ed_text   = view.findViewById(R.id.ed_text);
        tv_title = view.findViewById(R.id.tv_title);
    }


    public interface BottomSheetListener {
        void onSaveButtonClicked(String text, int type);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + " mus implement BottomSheetListener");
        }

    }
}