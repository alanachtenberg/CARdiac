package com.appspot.cadiac_404.androidapp;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.cadiac_404.androidapp.popup.PopUpDialogFragment;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import cardiac_404.appspot.com.ECG.cardiacApi.CardiacApi;
import cardiac_404.appspot.com.ECG.cardiacApi.model.ECGBean;
import cardiac_404.appspot.com.ECG.cardiacApi.model.ECGBeanCollection;

/**
 * Created by Alan on 9/21/2015.
 */
public class DeveloperFragment extends Fragment {
    private CardiacApi.ECG endpointsApi;
    private View mView;
    private Button mPopUpButton;
    private Button mInsertECG;
    private Button mListECG;
    private TextView mOutputTextView;

    public DeveloperFragment() {
    }

    public static DeveloperFragment newInstance() {
        DeveloperFragment fragment = new DeveloperFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CardiacApi.Builder endpointsBuilder = new CardiacApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null);
        endpointsBuilder.setRootUrl("https://cardiac-404.appspot.com/_ah/api/");
        endpointsBuilder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
            @Override
            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                abstractGoogleClientRequest.setDisableGZipContent(true);
            }
        });

        endpointsBuilder.setApplicationName("CARdiac");
        endpointsApi = endpointsBuilder.build().eCG();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_developer, container, false);
        mPopUpButton = (Button) mView.findViewById(R.id.pop_up_test_button);
        mInsertECG = (Button) mView.findViewById(R.id.insert_ecg_button);
        mListECG = (Button) mView.findViewById(R.id.list_ecg_button);
        mOutputTextView = (TextView) mView.findViewById(R.id.output_text);
        setUpButtons();
        return mView;
    }

    private void setUpButtons() {
        mPopUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt(PopUpDialogFragment.TIMEOUT_ARG_KEY, 10);
                PopUpDialogFragment popUp = new PopUpDialogFragment();
                popUp.setArguments(args);
                popUp.setResponseCallbacks(new PopUpDialogFragment.PopUpDialogFragmentIterface() {
                    @Override
                    public void negativeResponse() {
                        mOutputTextView.append("\nUser said they were NOT okay\n");
                    }

                    @Override
                    public void positiveResponse() {
                        mOutputTextView.append("\nUser said they were okay\n");
                    }

                    @Override
                    public void onTimeOut() {
                        mOutputTextView.append("\nUser response timed out\n");
                    }
                });
                popUp.show(getFragmentManager(), "pop_up_dialog_fragment");
            }
        });
        mInsertECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ECGBean bean = new ECGBean();
                            bean.setHeartRate(100);
                            bean.setProblemOne(false);
                            bean.setProblemTwo(false);
                            bean.setProblemThree(true);
                            endpointsApi.insertECG(bean).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        mListECG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<Void, Void, ECGBeanCollection> listECGTask = new AsyncTask<Void, Void, ECGBeanCollection>() {
                    @Override
                    protected ECGBeanCollection doInBackground(Void... params) {
                        try {
                            return endpointsApi.listECG().execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(ECGBeanCollection ecgBeans) {
                        super.onPostExecute(ecgBeans);
                        mOutputTextView.append("\nNew Response\n");
                        mOutputTextView.append(ecgBeans.getItems().get(ecgBeans.size()-1).toString());
                    }
                };
                listECGTask.execute();

            }
        });
    }


}
