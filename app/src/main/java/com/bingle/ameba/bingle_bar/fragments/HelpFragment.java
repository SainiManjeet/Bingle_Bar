package com.bingle.ameba.bingle_bar.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bingle.ameba.bingle_bar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ErAshwini on 18/5/18.
 */

public class HelpFragment extends Fragment {
    private static final String TAG = "Help";
    @BindView(R.id.webView_help)
    WebView webView;
    private ImageView map_icon_obj, drawer_icon;
    private DrawerLayout drawer_obj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_layout, container, false);

        ButterKnife.bind(this, view);
        startWebView("http://bingleinc.com/help.html");

        drawer_icon = view.findViewById(R.id.sett);
        drawer_obj = (DrawerLayout) view.findViewById(R.id.drawerlayout_id2);
        //visiblity set to gone beacuse we dont need to show this Icon

        map_icon_obj = view.findViewById(R.id.map_icon_id);
        map_icon_obj.setVisibility(View.GONE);


        drawer_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();


            }
        });

        return view;
    }

    private void startWebView(String url) {
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    /*if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }*/

                    progressDialog.dismiss();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });
        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }
}
