package org.devio.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.*;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.devio.takephoto.model.TImage;

import java.io.File;
import java.util.ArrayList;


/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.devio.org
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public class ResultActivity extends Activity {
    ArrayList<TImage> images;

    String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    String savePhotosPath = "/takePhotos/";
    String temp = "/temp/";
    FileHelper fhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_layout);
        images = (ArrayList<TImage>) getIntent().getSerializableExtra("images");
        showImg();

        Button savebtn_ = (Button)(findViewById(R.id.savebtn));
        savebtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImg();
            }
        });
    }

    private void showImg() {
        ImageView  imageV = (ImageView ) findViewById(R.id.imgShow1);
        String temPath = images.get(images.size() - 1).getCompressPath();
        Glide.with(this).load(new File(temPath)).into(imageV);
    }

    private void saveImg(){
        EditText photoNameTxt = (EditText)(findViewById(R.id.editTextTextPersonName));

        String photoNameStr = photoNameTxt.getText().toString();
        if(photoNameStr== null || photoNameStr.isEmpty()){
            Toast.makeText(this,"图像信息不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        fhelper = new FileHelper();
        String temPath = images.get(images.size() - 1).getCompressPath();
        String newPath = absolutePath + savePhotosPath ;
        String newFileName = photoNameStr + ".jpg";

        fhelper.moveFile(temPath,newPath,newFileName);
        fhelper.removeFile(absolutePath + temp);

        Intent intent = new Intent(this, SimpleActivity.class);
        startActivity(intent);
    }
}
