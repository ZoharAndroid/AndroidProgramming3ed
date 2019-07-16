package com.zohar.criminalintent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";
    private final String DIALOG_SHOW = "dialog_show";
    private final String DIALOG_SHOW_TIME = "dialog_show_time";
    private final String DIALOG_SHOW_PHOTO = "dialog_show_photo";

    private static final String ARGS_CRIME_ID = "crime_id";

    private static final int REQUEST_CONTACT_CODE = 3;
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_TIME = 2;
    private static final int PERMISSION_REQUEST_CODE = 4;
    private static final int PERMISSION_REQUSET_CODE_CALL = 6;
    private static final int REQUEST_READ_CONTACT_CODE = 5;
    private static final int REQUSET_CODE_PHOTH = 7;
    private static final int REQUEST_CODE_PHOTO_DIALOG = 8;

    private Crime mCrime;
    private File mCrimePhotoFile;

    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button mSuspentButton;
    private Button mReportButton;
    private Button mCallButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoImage;

    private String mPhone;
    private int mViewWidth;
    private int mViewHeight;

    public static Fragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID crimeId = (UUID) getArguments().getSerializable(ARGS_CRIME_ID);
        mCrime = CrimeLab.getInstance(getContext()).getCrime(crimeId);
        mCrimePhotoFile = CrimeLab.getInstance(getContext()).getPhotoFile(mCrime);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cirme, container, false);

        mTitleField = view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged -> " + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged -> " + s.toString());
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged -> " + s.toString());
            }
        });

        mDateButton = view.findViewById(R.id.crime_date);
        updateDate();

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = (DatePickerFragment) DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_CODE);
                dialog.show(manager, DIALOG_SHOW);
            }
        });

        mTimeButton = view.findViewById(R.id.crime_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment timeDiaolg = (TimePickerFragment) TimePickerFragment.newInstance(mCrime.getDate());
                timeDiaolg.setTargetFragment(CrimeFragment.this, REQUEST_CODE_TIME);
                timeDiaolg.show(manager, DIALOG_SHOW_TIME);
            }
        });

        // 打开联系人并且返回联系人数据
        mSuspentButton = view.findViewById(R.id.crime_suspend);
        final Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        intent.addCategory(Intent.CATEGORY_HOME);
        mSuspentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, REQUEST_CONTACT_CODE);
            }
        });

        if (mCrime.getMsuspend() != null) {
            mSuspentButton.setText(mCrime.getMsuspend());
        }

        // 如果没有读取联系人的软件，那么这个按钮是不可用的
        PackageManager manager = getActivity().getPackageManager();
        if (manager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspentButton.setEnabled(false);
        }

        // 发送短信
        mReportButton = view.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                intent = Intent.createChooser(intent, getString(R.string.send_report));
//                startActivity(intent);

                ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(getActivity());
                intentBuilder.setText("text/plain");
                intentBuilder.setText(getCrimeReport());
                intentBuilder.setSubject(getString(R.string.crime_report_subject));
                intentBuilder.createChooserIntent();
                intentBuilder.startChooser();
            }
        });

        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        // 打电话按钮
        mCallButton = view.findViewById(R.id.crime_call);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检查读取联系人权限
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有权限，那么就进行权限受理
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);
                } else {
                    readContact();
                }
            }
        });


        mPhotoButton = view.findViewById(R.id.crime_camera);
        // 检测是否有相机可用
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = (captureIntent.resolveActivity(manager) != null && mCrimePhotoFile != null);
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.zohar.criminalintent.fileprovider", mCrimePhotoFile);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> carmareActivies = getActivity().getPackageManager().queryIntentActivities(captureIntent, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : carmareActivies) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureIntent, REQUSET_CODE_PHOTH);
            }
        });

        // 更新图片
        mPhotoImage = view.findViewById(R.id.crime_photo);
        updatePhotoView();
        mPhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                PhotoFragment photoDialog = (PhotoFragment) PhotoFragment.newInstance(mCrimePhotoFile);
                photoDialog.setTargetFragment(CrimeFragment.this, REQUEST_CODE_PHOTO_DIALOG);
                photoDialog.show(fragmentManager, DIALOG_SHOW_PHOTO);
            }
        });

        final ViewTreeObserver viewTreeObserver = mPhotoImage.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //判断ViewTreeObserver 是否alive，如果存活的话移除这个观察者
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                    //获得宽高
                    mViewWidth = mPhotoImage.getMeasuredWidth();
                    mViewHeight = mPhotoImage.getMeasuredHeight();
                    Log.d(TAG, "onGlobalLayout-> width = " + mViewWidth + "; height = " + mViewHeight);
                    //updatePhotoVie);
                }
            }
        });

        return view;
    }

    /**
     * 读取联系人号码
     */
    private void readContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, REQUEST_READ_CONTACT_CODE);
    }

    /**
     * 拨打电话
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContact();
                } else {
                    Toast.makeText(getContext(), "权限受理失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSION_REQUSET_CODE_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContact();
                } else {
                    Toast.makeText(getContext(), "权限受理失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.crime_remove:
                CrimeLab.getInstance(getContext()).deleteCrime(mCrime);
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }

        if (requestCode == REQUEST_CODE_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateTime();
        }

        if (requestCode == REQUEST_CONTACT_CODE && data != null) {
            Uri uri = data.getData();
            Log.d(TAG, uri.toString());// content://com.android.contacts/contacts/lookup/0r2-4F51/2
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(uri, queryFields, null, null, null, null);

                if (cursor.getCount() == 0) {
                    return;
                }

                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                mCrime.setMsuspend(suspect);
                mSuspentButton.setText(suspect);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        // 读取联系人
        if (requestCode == REQUEST_READ_CONTACT_CODE && data != null) {
            Uri uri = data.getData();
            Log.d(TAG, "REQUEST_READ_CONTACT_CODE : " + uri); // content://com.android.contacts/data/3
            Cursor cursor = null;
            try {
                String[] queryFields = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, queryFields, null, null, null, null);
                cursor.moveToFirst();
                String phone = cursor.getString(0);
                // 调用拨打电话
                requestCallPermission(phone);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        // 请求相机拍照后的图片
        if (requestCode == REQUSET_CODE_PHOTH) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.zohar.criminalintent.fileprovider", mCrimePhotoFile);
            // 解析权限
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            // 更新视图
            updatePhotoView(mViewWidth, mViewHeight);
        }
    }

    private void requestCallPermission(String phone) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //去申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUSET_CODE_CALL);
        } else {
            call(phone);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 更新数据库中的内容
        CrimeLab.getInstance(getContext()).updateCrime(mCrime);
    }


    /**
     * 更新日期
     */
    private void updateDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd E", Locale.CHINA);
        String date = format.format(mCrime.getDate());
        mDateButton.setText(date);
    }

    /**
     * 更新时间
     */
    private void updateTime() {
        DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        String time = format.format(mCrime.getDate());
        mTimeButton.setText(time);
    }

    /**
     * 获取report内容
     *
     * @return
     */
    private String getCrimeReport() {
        String sovledString = null;
        if (mCrime.isSolved()) {
            sovledString = getString(R.string.crime_report_solved);
        } else {
            sovledString = getString(R.string.crime_report_unsolved);
        }

        String dateFormate = "EEE, MMM dd";
        String dateString = new SimpleDateFormat(dateFormate, Locale.CHINA).toString();

        String suspect = mCrime.getMsuspend();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        return getString(R.string.crime_report, mCrime.getTitle(), dateString, sovledString, suspect);
    }

    /**
     * 更新图片显示
     */
    private void updatePhotoView() {
        if (mCrimePhotoFile == null && mCrimePhotoFile.exists()) {
            mPhotoImage.setImageDrawable(null);
        } else {
            mPhotoImage.setImageBitmap(PictureUtils.getBitmapScale(mCrimePhotoFile.getPath(), getActivity()));
        }
    }

    private void updatePhotoView(int width , int height){
        if (mCrimePhotoFile == null){
            mPhotoImage.setImageDrawable(null);
        }else{
            mPhotoImage.setImageBitmap(PictureUtils.getBitmapScale(mCrimePhotoFile.getPath(), width,height));
        }
    }
}
