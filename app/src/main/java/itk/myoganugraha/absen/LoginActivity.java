package itk.myoganugraha.absen;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.net.NetworkInterface;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private Button btnLinkRegist, btnLogin;
    private EditText usernameLog, passwordLog;
    private TextView macAddres, fingerprintChecked;
    private ProgressDialog loading;
    private Context mContext;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        initComponents();

    }




    private void initComponents() {

        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutLogin);
        //ngecek hpnya support fingerprint atau engga
        fingerprintCheck();

        btnLinkRegist = (Button) findViewById(R.id.btnToRegister);
        btnLinkRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegisterAct = new Intent(mContext, RegisterActivity.class);
                startActivity(toRegisterAct);
                finish();
            }
        });

        macAddres = (TextView) findViewById(R.id.macAddres);
        if (getWlanMacAddress() != null) {
            macAddres.setVisibility(View.VISIBLE);
            macAddres.setText("Your MAC Address : " + getWlanMacAddress());
        } else {
            macAddres.setVisibility(View.GONE);
        }

        usernameLog = (EditText) findViewById(R.id.usernameLog);
        passwordLog = (EditText) findViewById(R.id.passwordLog);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMainAct = new Intent(mContext, MainActivity.class);
                startActivity(toMainAct);
                finish();
            }
        });

    }

    private void fingerprintCheck() {
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(mContext);


        if (!fingerprintManagerCompat.isHardwareDetected()) {
            final Snackbar snackbar = Snackbar
                    .make(linearLayout, "No Fingerprint Support on This Device", Snackbar.LENGTH_INDEFINITE).setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            snackbar.show();

        } else if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
            FancyToast.makeText(this,"No Fingerprint Set",FancyToast.LENGTH_SHORT,FancyToast.CONFUSING,true);
        } else {
            Snackbar snackbar = Snackbar
                    .make(linearLayout, "This Device Support Fingerprint", Snackbar.LENGTH_INDEFINITE).setAction("All Is Well", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            snackbar.show();
        }
    }


    public static String getWlanMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


}

