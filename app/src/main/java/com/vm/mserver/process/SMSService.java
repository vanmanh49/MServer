package com.vm.mserver.process;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.vm.mserver.dao.CRUD;
import com.vm.mserver.model.Student;

import java.util.StringTokenizer;

/**
 * Created by VanManh on 05-Dec-17.
 */

public class SMSService extends IntentService {

    private CRUD crud;
    private String rs;

    public SMSService() {
        super(SMSService.class.getName());
        crud = CRUD.getInstants(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final String phonNum = intent.getStringExtra("phoneNum");
        final String mess = intent.getStringExtra("mess");


        if (crud.isPhoneNumExist(phonNum)) {
            rs = processMess(mess);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phonNum, null, rs, null, null);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Đã gửi sms tới: " + phonNum + "\nNội dung: " + rs, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            rs = "Số điện thoại chưa đăng ký dịch vụ!";
        }
    }

    private String processMess(String mess) {
        String rs = "";
        StringTokenizer tokenizer = new StringTokenizer(mess);
        if (tokenizer.countTokens() == 2) {
            String command = (String) tokenizer.nextElement();
            String mssv = (String) tokenizer.nextElement();
            if (command.equalsIgnoreCase("tracuu")) {
                try {
                    int MSSV = Integer.parseInt(mssv);
                    Student student = crud.getStudentmarkByMSSV(MSSV);
                    rs = "MS: " + student.getMs()
                            + "\nTên: " + student.getName()
                            + "\nToán: " + student.getToan()
                            + "\nLý: " + student.getLy()
                            + "\nHóa: " + student.getHoa();
                } catch (Exception e) {
                    rs = "Không tồn tại mssv trong database!";
                }
            } else {
                rs = "Sai cú pháp! \"tracuu mssv\" trong đó mssv: là mã số sinh viên của bạn";
            }
        } else {
            rs = "Sai cú pháp! \"tracuu mssv\" trong đó mssv: là mã số sinh viên của bạn";
        }
        return rs;
    }
}
