package com.example.instagramclone;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class storyfragment extends Fragment {
    @Nullable
    List storyids;
    LinearLayout seekbar_view;
    ImageView storyimage;
    int width_seekbar;
    ValueAnimator anim;
    SeekBar v;
    int i;
    int seconds_count;
    int start_seconds;
    storyfragment(List storyid){
        this.storyids=storyid;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story, container, false);

    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        seekbar_view=view.findViewById(R.id.seekbar_view);

        seekbar_view.post(new Runnable()
        {

            @Override
            public void run()
            {
                width_seekbar=seekbar_view.getWidth();
                Log.i("TEST", "Layout width : "+ seekbar_view.getWidth());
                int no_of_seekbars=storyids.size();
                int width_seekbar_single=width_seekbar/no_of_seekbars;
                Log.i("wodth",width_seekbar_single+"");
                for(int i=0;i<no_of_seekbars;i++){
                    SeekBar seekBar=new SeekBar(getContext());
                    seekBar.setTag(i);
                    if (seekBar.isActivated()) {
                        seekBar.getThumb().mutate().setAlpha(255);
                    } else {
                        seekBar.getThumb().mutate().setAlpha(0);
                    }
                    seekbar_view.addView(seekBar,width_seekbar_single,100);


                }

            }
        });

        storyimage=view.findViewById(R.id.story_image);
        //SeekBar seekBar=view.findViewById(R.id.seekBar);

        /*new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //getFragmentManager().popBackStackImmediate();
            }
        }.start();*/
        ParseQuery<ParseObject> query= ParseQuery.getQuery("Story");
        String[] arr=storyids.get(0).toString().split("-");
        //query.whereEqualTo("username",);
        query.whereMatches("username","^"+arr[0]);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0) {
                        for (ParseObject object : objects) {
                        if (object.get("story") == null) {
                            storyimage.setImageResource(R.drawable.like);
                        } else {
                            ParseFile file = (ParseFile) object.get("story");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null) {
                                        int time=storyids.size();
                                        i=0;
                                        seconds_count=0;
                                         start_seconds=time*10;
                                        new CountDownTimer(time*10000, 1000) {
                                            public void onTick(long millisUntilFinished) {
                                                long seconds=millisUntilFinished/1000;
                                                Log.i("timer",seconds+"");
                                               if(seconds<=start_seconds) {
                                                        v=(SeekBar)seekbar_view.getChildAt(i);
                                                        Log.i("this is seekbar",v+"");
                                                        if(v!=null) {
                                                            anim = ValueAnimator.ofInt(0, v.getMax());
                                                            anim.setDuration(9000);
                                                            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                                @Override
                                                                public void onAnimationUpdate(ValueAnimator animation) {
                                                                    int animProgress = (Integer) animation.getAnimatedValue();
                                                                    v.setProgress(animProgress);
                                                                }
                                                            });

                                                            anim.start();
                                                        }
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                    storyimage.setImageBitmap(bitmap);

                                                    Log.i("i value",i+"");
                                                    i++;
                                                    start_seconds=start_seconds-10;
                                                }

                                            }
                                            public void onFinish() {
                                                Fragment selectedFragment=new HomeFragment();
                                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                transaction.replace(R.id.Fragment_container,selectedFragment);
                                                transaction.addToBackStack(null);
                                                transaction.commit();
                                            }
                                        }.start();

                                    }
                                }
                            });
                        }
                    }
                    }
                }
            }
        });

    }
}

