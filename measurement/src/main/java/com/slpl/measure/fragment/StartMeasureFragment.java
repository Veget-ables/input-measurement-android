package com.slpl.measure.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.slpl.measure.R;

public class StartMeasureFragment extends Fragment implements View.OnClickListener {
    private StartMeasureFragment.OnFragmentInteractionListener mListener;
    private static final String SHOW_WORD = "SHOW_WORD";
    private String mText;

    public StartMeasureFragment() {// Fragmentには空のコンストラクタが必要
    }

    public static StartMeasureFragment newInstance(String words) {
        StartMeasureFragment fragment = new StartMeasureFragment();
        Bundle args = new Bundle();
        args.putString(SHOW_WORD, words);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mText = getArguments().getString(SHOW_WORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_measure, container, false);
        ((TextView) view.findViewById(R.id.tvText)).setText(mText);
        view.findViewById(R.id.civStart).setOnClickListener(this);
        view.findViewById(R.id.civCancel).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }
        boolean isStart = (view.getId() == R.id.civStart);
        mListener.onInteractionStartMeasure(isStart);
        killFragment();
    }

    private void killFragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StartMeasureFragment.OnFragmentInteractionListener) {
            mListener = (StartMeasureFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "ActivityからListenerが届いてないで。");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        /**
         * 計測開始をActivityに返す
         */
        void onInteractionStartMeasure(boolean isStart);
    }
}
