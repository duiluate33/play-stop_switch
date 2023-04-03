package kr.ac.yeonsung.seoj.mobile1project_1205_02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView list1;
    Button btnPlay, btnStop, btnPause;
    TextView textMusic;
    ProgressBar progressMusic;
    int position;

    ArrayList<String> arrList;
    String selectedMusic;
    String musicPath = Environment.getExternalStorageDirectory().getPath() + "/";
    //getExternalStorageDirectory() 파일을 반환받아옴
    MediaPlayer player;
    boolean PAUSED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Music Player");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        arrList = new ArrayList<String>();

        File[] files = new File(musicPath).listFiles(); //이 경로에 있는 파일 목록들을 파일에 저장
        String FileName, fileExt;
        for(File file: files) {
            FileName = file.getName();
            fileExt = FileName.substring(FileName.length() - 3);
            //전체길이에서 -3뺀 길이를 Ext에 저장
            if (fileExt.equals("mp3")) {
                arrList.add(FileName);
            }
            list1 = findViewById(R.id.list1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrList);
            list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            list1.setAdapter(adapter);
            list1.setItemChecked(0, true); //선택한 아이템과 포지션 선택 -> 선택된 상태
            selectedMusic = arrList.get(0); //첫번째 항목에 음악 파일 저장
            list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    //int i = 선택된 아이디 번호
                    //선택돈 항목의 음악을 알아야 재생하므로 i->position으로 변경
                    selectedMusic = arrList.get(position);
                }
            });

            btnPlay = findViewById(R.id.btn_play);
            btnStop = findViewById(R.id.btn_stop);
            btnPause = findViewById(R.id.btn_pause);
            textMusic = findViewById(R.id.text_music);
            progressMusic = findViewById(R.id.progress_music);
            btnStop.setClickable(false); //처음 실행할때 필요

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(musicPath + selectedMusic);
                        player.prepare();
                        player.start();
                        btnPlay.setClickable(false);
                        btnStop.setClickable(true); //스탑을 누르면 플레이를 활성화시킬 수 있음
                        btnPause.setClickable(true);
                        textMusic.setText("실행 중인 음악: " + selectedMusic);
                        progressMusic.setVisibility(View.VISIBLE);

                    } catch (IOException e) {
                        e.printStackTrace(); //예외처리
                    }
                    if (player.isPlaying()) {
                        player.seekTo(position);
                        player.start();
                    } else {
                        player.start();
                        btnPlay.setClickable(false);
                        btnStop.setClickable(true);
                        textMusic.setText("실행중인 음악 : " + selectedMusic);
                        progressMusic.setVisibility(View.VISIBLE);
                    }
                }
            });

            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player.stop();
                    player.reset();
                    btnPlay.setClickable(true);
                    btnStop.setClickable(false);
                    btnPause.setClickable(false);
                    textMusic.setText("실행 중인 음악: " + selectedMusic);
                    progressMusic.setVisibility(View.INVISIBLE);
                }
            });

            btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.pause();
                    position = player.getCurrentPosition();
                }
            });
            btnPause.setClickable(false);
        }
    }
}