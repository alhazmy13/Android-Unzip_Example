package net.alhazmy13.unzipfile;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private EditText filePathEditText,fileUrlEditText;
    private Button unzipButton,selectFileButton,downloadButton;
    private File sdCardStorage;
    private static int ACTIVITY_CODE=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        sdCardStorage = Environment.getExternalStorageDirectory();
        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileUrlEditText.setText("");
                pickFile();
            }
        });
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isValidURL(fileUrlEditText.getText().toString())){
                    Toast.makeText(MainActivity.this,"Please enter a valid URL",Toast.LENGTH_SHORT).show();
                    return;
                }
                new DownloadTask(MainActivity.this).execute(fileUrlEditText.getText().toString());
            }
        });
        unzipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(MainActivity.this,"Extract files to "+sdCardStorage.getPath(),Toast.LENGTH_SHORT).show();
                    Zip.unzip(new File(filePathEditText.getText().toString()), new File(sdCardStorage.getPath()));
                    Toast.makeText(MainActivity.this,"Done,,",Toast.LENGTH_SHORT).show();
                }catch (Exception IOException){
                    Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initViews(){
        filePathEditText=(EditText)findViewById(R.id.filePathEditText);
        fileUrlEditText=(EditText)findViewById(R.id.filrURLEditText);
        unzipButton=(Button)findViewById(R.id.unzipButton);
        selectFileButton=(Button)findViewById(R.id.selectFileButton);
        downloadButton=(Button)findViewById(R.id.downloadButton);
    }

    private void pickFile() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("file/zip");
        startActivityForResult(i, ACTIVITY_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==ACTIVITY_CODE) {
            if (!isZipFile(data.getData().getPath())) {
                Toast.makeText(MainActivity.this, "Please Select .zip file", Toast.LENGTH_SHORT).show();
                return;
            }
            filePathEditText.setText(data.getData().getPath().toString());
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    protected boolean isZipFile(String path){
        return path.matches(".*\\.zip");
    }

    protected boolean isValidURL(String path){
        return path.matches("http:\\/\\/.*.zip");
    }

    public void updatePath(String path){
        filePathEditText.setText(path+"");
    }



}
