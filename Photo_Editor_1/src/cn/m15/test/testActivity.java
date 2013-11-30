package cn.m15.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class testActivity extends Activity {
	
    public static final int NONE = 0;
    public static final int CAMERA = 1; 
    public static final int ALBUMSel = 2;
    public static final int PHOTORESULT = 3;

    private String selectedImagePath;

    public static final String IMAGE_UNSPECIFIED = "image/*";
    ImageView imageView = null;
    Button button0 = null;
    Button button1 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        imageView = (ImageView) findViewById(R.id.imageID);
        button0 = (Button) findViewById(R.id.btn_01);
        button1 = (Button) findViewById(R.id.btn_02);

        button0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(intent, ALBUMSel);
            }
        });

        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                startActivityForResult(intent, CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;

        if (requestCode == CAMERA) {
        	selectedImagePath=Environment.getExternalStorageDirectory()+"/temp.jpg";
            Intent newintent = new Intent(testActivity.this, ImgEditor.class);
            newintent.putExtra("imagePath", selectedImagePath);
            startActivity(newintent);
        }

        if (data == null)
            return;

        if (requestCode == ALBUMSel) {
            Uri selectedImageUri = data.getData();
            selectedImagePath = getPath(selectedImageUri);
            Intent newintent = new Intent(testActivity.this, ImgEditor.class);
            newintent.putExtra("imagePath", selectedImagePath);
            startActivity(newintent);
        }

//        if (requestCode == PHOTORESULT) {
//            Bundle extras = data.getExtras();
//            if (extras != null) {
//                Bitmap photo = extras.getParcelable("data");
//                imageView.setImageBitmap(photo);
//            }
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}