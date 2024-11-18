package com.example.webbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText editTextUrl;
    private WebView webViewMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUrl = findViewById(R.id.activity_main__editTextUrl);
        webViewMain = findViewById(R.id.activity_main__webViewMain);

        // 웹 세팅 객체 가져와서
        WebSettings webSettings = webViewMain.getSettings();
        // 수정한다(자바스크립트 엔진 활성화)
        webSettings.setJavaScriptEnabled(true);

        webViewMain.setWebViewClient(new WebViewClient());

        // 에디터 액션 클릭하면 아래 메서드 실행된다
        editTextUrl.setOnEditorActionListener((v, actionId, event)-> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 검색
                String url = editTextUrl.getText().toString().trim();

                if(url.startsWith("https://") == false && url.startsWith("https://") == false) {
                    url = "http://" + url;

                    editTextUrl.setText(url);
                }

                goToUrl(url);

                // 키보드 내리기
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return true;
            }

            return false;
        });
    }

    private void goToUrl(String url) {
        webViewMain.loadUrl(url);
    }

    public void refreshButtonClicked(View view) {
        webViewMain.reload();
    }
}