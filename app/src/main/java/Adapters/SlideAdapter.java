package Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pat.sd.duomaya.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.ArrayList;

import Clases.Palabras;

/**
 * Created by danie on 21/04/2018.
 */

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    Activity activity;
    int[] lst_Colors;
    ArrayList<Palabras> palabras;

    public SlideAdapter(Context context, ArrayList<Palabras> palabras, Activity activity, int[] colors)
    {
        this.context = context;
        this.palabras = palabras;
        this.activity = activity;
        lst_Colors = colors;
    }

    @Override
    public int getCount() {
        return palabras.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view ==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide, container, false);
        LinearLayout layoutSlide = view.findViewById(R.id.sliderLinearLayout);
        ImageView imgSlide = view.findViewById(R.id.imgSlide);
        TextView txtMaya = view.findViewById(R.id.txtMaya);
        TextView txtEsp = view.findViewById(R.id.txtEsp);
        
        layoutSlide.setBackgroundColor(lst_Colors[position]);
        Picasso.get().load(palabras.get(position).ImagenCont).transform(new CircleTransform()).into(imgSlide);
        imgSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(palabras.get(position).AudioCont);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        txtEsp.setText(palabras.get(position).PalabraEsp);
        txtMaya.setText(palabras.get(position).PalabraMaya);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }


}
