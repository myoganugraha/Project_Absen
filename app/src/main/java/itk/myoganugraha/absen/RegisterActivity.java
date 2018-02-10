package itk.myoganugraha.absen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    
    private Context mContext;
    Button registerNow, toLogin;
    EditText firstName, lastName, email, password, phone;
    TextView macAddresReg, fingerprintRegisterChecked;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        mContext = this;
        initComponents();
    }

    private void initComponents() {
        //ngecek hpnya support fingerprint atau engga
        fingerprintCheck();

        toLogin = (Button) findViewById(R.id.btnToLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginAct = new Intent(mContext, LoginActivity.class);
                startActivity(toLoginAct);
                finish();
            }
        });

        macAddresReg = (TextView) findViewById(R.id.macAddresReg);if(getWlanMacAddress() != null){
            macAddresReg.setVisibility(View.VISIBLE);
            macAddresReg.setText("Your MAC Address : " + getWlanMacAddress());
        }
        else{
            macAddresReg.setVisibility(View.GONE);
        }

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.emailReg);
        password = (EditText) findViewById(R.id.passwordReg);
        phone = (EditText) findViewById(R.id.phoneNumber);

        registerNow = (Button) findViewById(R.id.btnRegister);
    }

    private void fingerprintCheck() {
        fingerprintRegisterChecked = (TextView) findViewById(R.id.fingerprintRegisterCheck);

        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(mContext);


        if (!fingerprintManagerCompat.isHardwareDetected()) {
            fingerprintRegisterChecked.setVisibility(View.VISIBLE);
            fingerprintRegisterChecked.setText("No Fingerprint Support For Your Device");
        } else if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
            FancyToast.makeText(this,"No Fingerprint Set",FancyToast.LENGTH_SHORT,FancyToast.CONFUSING,true);
        } else {
            fingerprintRegisterChecked.setVisibility(View.VISIBLE);
            fingerprintRegisterChecked.setText("Your Device Support Fingerprint");
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
