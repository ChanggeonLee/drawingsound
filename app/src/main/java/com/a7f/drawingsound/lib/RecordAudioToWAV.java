package com.a7f.drawingsound.lib;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import ca.uol.aig.fftpack.RealDoubleFFT;

public class RecordAudioToWAV extends AsyncTask<Void, double[], Void> {

    private final int RECORDER_SAMPLERATE = 44100;
    private final int RECORDER_CHANNELS = AudioFormat.CHANNEL_CONFIGURATION_MONO;  //안드로이드 녹음시 채널 상수값
    private final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private final int BUFFER_SIZE = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

    private final int WAVE_CHANNEL_MONO = 1;  //wav 파일 헤더 생성시 채널 상수값
    private final int HEADER_SIZE = 0x2c;
    private final int RECORDER_BPP = 16;

    private final String TEMP_FILE_NAME = "test_temp.bak";
    private boolean started = false;
    private String mFileName = "20191108";
    private int mAudioLen = 0;

    private BufferedInputStream mBIStream;
    private BufferedOutputStream mBOStream;


    // fft test
    private RealDoubleFFT transformer;
    private int myBufferSize = AudioTrack.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
    private int noteIndex = 0;
    private List<String> note;
    private TextView TextViewFFT;


    // **
    private File waveFile;
    private File tempFile;
    int read;
    byte[] buffer2;
    byte[] data;
    // **

    public RecordAudioToWAV(TextView TextViewFFT){
        transformer = new RealDoubleFFT(BUFFER_SIZE);
        this.TextViewFFT = TextViewFFT;
        note = new ArrayList<String>();
    }

    public boolean getStarted(){
        return started;
    }

    public void setStarted( boolean started){
        this.started = started;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                    RECORDER_AUDIO_ENCODING, BUFFER_SIZE);

            short[] buffer = new short[BUFFER_SIZE];
            double[] toTransform = new double[BUFFER_SIZE];

            // **
            // 임시 저장 버퍼, 음성 데이터 들어갈 데이터
            buffer2 = new byte[BUFFER_SIZE];
            data = new byte[BUFFER_SIZE];

            // 저장할 wave 파일과 temp 파일
            waveFile = new File(Environment.getExternalStorageDirectory()+"/"+mFileName+".wav");
            tempFile = new File(Environment.getExternalStorageDirectory()+"/"+TEMP_FILE_NAME);

            // temp파일로 아웃풋 스트림 설정
            try {
                mBOStream = new BufferedOutputStream(new FileOutputStream(tempFile));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            read = 0;
            // **

            audioRecord.startRecording();
            while (started) {
                int bufferReadResult = audioRecord.read(buffer, 0, BUFFER_SIZE);
                for (int i = 0; i < BUFFER_SIZE && i < bufferReadResult; i++) {
                    toTransform[i] = (double) buffer[i] / Short.MAX_VALUE; // 부호 있는 16비트
                }
                transformer.ft(toTransform);
                publishProgress(toTransform);

                // **
                if(mBOStream != null){
                    // 녹음중일 동안 데이터를 읽고, temp 파일(앞에서 아웃풋스트림으로 설정함)에 쓴다.
                    read = audioRecord.read(data, 0, BUFFER_SIZE);
                    if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                        mBOStream.write(data);
                    }
                }
                // **

            }

            audioRecord.stop();
        } catch (Throwable t) {
            Log.e("AudioRecord", "Recording Failed");
        }
        return null;
//        return note.toString();
    }

    // **
    public void transferToWAV(){
        if (null != mBOStream) {
            try {
                // 녹음이 끝나면 temp파일의 길이를 계산한다.
                mBOStream.flush();
                mAudioLen = (int)tempFile.length();

                // 인풋스트림으로 temp 파일을 설정하고, 아웃풋스트림은 닫는다.
                mBIStream = new BufferedInputStream(new FileInputStream(tempFile));
                mBOStream.close();

                // 아웃풋 스트림을 wave 파일로 새로 설정하고, 헤더를 쓴다.
                mBOStream = new BufferedOutputStream(new FileOutputStream(waveFile));
                mBOStream.write(getFileHeader());
                //len = HEADER_SIZE;

                // 인풋 스트림에 있는 것을 읽어서, 아웃풋 스트림에 쓴다.
                while ((read = mBIStream.read(buffer2)) != -1) {
                    mBOStream.write(buffer2);
                }

                // 모두 닫는다.
                mBOStream.flush();
                mBIStream.close();
                mBOStream.close();

                Log.d("파일쓰기","success!!!!!!");

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
    // **

    private byte[] getFileHeader() {
        // temp에 읽어들인 길이 + 헤더길이(40)으로 총 데이터 길이를 잡는다.
        byte[] header = new byte[HEADER_SIZE];
        int totalDataLen = mAudioLen + 40;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * WAVE_CHANNEL_MONO/8;

        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = (byte)1;  // format = 1 (PCM방식)
        header[21] = 0;
        header[22] =  WAVE_CHANNEL_MONO;
        header[23] = 0;
        header[24] = (byte) (RECORDER_SAMPLERATE & 0xff);
        header[25] = (byte) ((RECORDER_SAMPLERATE >> 8) & 0xff);
        header[26] = (byte) ((RECORDER_SAMPLERATE >> 16) & 0xff);
        header[27] = (byte) ((RECORDER_SAMPLERATE >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) RECORDER_BPP * WAVE_CHANNEL_MONO/8;  // block align
        header[33] = 0;
        header[34] = RECORDER_BPP;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte)(mAudioLen & 0xff);
        header[41] = (byte)((mAudioLen >> 8) & 0xff);
        header[42] = (byte)((mAudioLen >> 16) & 0xff);
        header[43] = (byte)((mAudioLen >> 24) & 0xff);
        return header;

    }


    // **

    @Override
    protected void onProgressUpdate(double[]... toTransform) {
        int max = 0;
        int index = 0;
        Log.d("length",Integer.toString(toTransform[0].length));
        for(int i = 0; i < toTransform[0].length; i++){
            if(max < toTransform[0][i]){
                max = (int)toTransform[0][i];
                index = i;
            }
        }
        Log.d("frequency",Double.toString(index*4.6));
        getNote(index*4.6);

    }

    private void getNote(double fre){
        String[] AllNote = {"C3","C#3","D3","D#3","E3","F3","F#3","G3","G#3","A3","A#3","B3",
                "C4","C#4","D4","D#4","E4","F4","F#4","G4","G#4","A4","A#4","B4",
                "C5","C#5","D5","D#5","E5","F5","F#5","G5","G#5","A5","A#5","B5"};
        String Note ;

        fre = (int)fre;

        if(180 <= fre && fre < 196){
            Note = "C";
        } else if(196 <= fre && fre < 205){
            Note = "^C";
        } else if(205 <= fre && fre < 215){
            Note = "D";
        } else if(215 <= fre && fre < 236){
            Note = "^D";
        } else if(236 <= fre && fre < 248){
            Note = "E";
        } else if(248 <= fre && fre < 268){
            Note = "F";
        } else if(268 <= fre && fre < 286){
            Note = "^F";
        } else if(286 <= fre && fre < 305){
            Note = "G";
        } else if(305 <= fre && fre < 314){
            Note = "^G";
        } else if(314 <= fre && fre < 333){
            Note = "A";
        } else if(333 <= fre && fre < 351){
            Note = "^A";
        } else if(351 <= fre && fre < 378){
            Note = "B";
        }
        // 높은음
        else if(378 <= fre && fre < 408){
            Note = "c";
        } else if(408 <= fre && fre < 430){
            Note = "^c";
        } else if(430 <= fre && fre < 456){
            Note = "d";
        } else if(456 <= fre && fre < 475){
            Note = "^d";
        } else if(475 <= fre && fre < 505){
            Note = "e";
        } else if(505 <= fre && fre < 540){
            Note = "f";
        } else if(540 <= fre && fre < 565){
            Note = "^f";
        } else if(565 <= fre && fre < 610){
            Note = "g";
        } else if(610 <= fre && fre < 620){
            Note = "^g";
        } else if(620 <= fre && fre < 670){
            Note = "a";
        } else if(670 <= fre && fre < 710){
            Note = "^a";
        } else if(710 <= fre && fre < 750){
            Note = "b";
        } else {
            Note = "z";
        }

        note.add(Note);
        //        noteIndex++;
        //        Log.d("index",Integer.toString(noteIndex));
        TextViewFFT.setText(Note);
    }

    public List<String> getNoteData(){
//        String str = String.join(",", note);
//        Log.e("inRecordNote",note);
        return note;

    }
}




/*
public class RecordAudioToWAV implements Runnable {
    private final int RECORDER_SAMPLERATE = 0xac44;
    private final int RECORDER_CHANNELS = AudioFormat.CHANNEL_CONFIGURATION_MONO;  //안드로이드 녹음시 채널 상수값
    private final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private final int BUFFER_SIZE;

    private final int WAVE_CHANNEL_MONO = 1;  //wav 파일 헤더 생성시 채널 상수값
    private final int HEADER_SIZE = 0x2c;
    private final int RECORDER_BPP = 16;

    private final String TEMP_FILE_NAME = "test_temp.bak";

    private AudioRecord mAudioRecord;
    private boolean mIsRecording;
    private String mFileName;

    private BufferedInputStream mBIStream;
    private BufferedOutputStream mBOStream;

    private int mAudioLen = 0;

    public RecordAudioToWAV(String fileName) {
        super();
        mFileName = fileName;
        BUFFER_SIZE = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
        mIsRecording = false;
    }

    @Override
    public void run() {
        // 녹음 객체 생성
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BUFFER_SIZE);

        // 녹음 시작
        mAudioRecord.startRecording();
        mIsRecording = true;

        // 기기에 파일 쓰기
        writeAudioDataToFile();
    }

    // 기기에 파일 쓰기
    // AudioRecorder 인스턴스에 저장된 데이터를 임시파일에 읽어온다.
    // 읽은 뒤 임시파일의 크기를 계산한다.
    // 임시파일의 크기로 wav 파일 헤더를 결정한다.
    // wav 포맷의 헤더 파일을 삽입한다.
    private void writeAudioDataToFile() {
        byte[] buffer = new byte[BUFFER_SIZE];
        byte[] data = new byte[BUFFER_SIZE];

        // 저장할 wave 파일과 temp 파일
        File waveFile = new File(Environment.getExternalStorageDirectory()+"/"+mFileName);
        File tempFile = new File(Environment.getExternalStorageDirectory()+"/"+TEMP_FILE_NAME);

        // temp파일로 아웃풋 스트림 설정
        try {
            mBOStream = new BufferedOutputStream(new FileOutputStream(tempFile));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        int read = 0;
        //int len = 0;

        // 읽어 온 값이 있으면,
        if (null != mBOStream) {
            try {
                // 녹음중일 동안 데이터를 읽고, temp 파일(앞에서 아웃풋스트림으로 설정함)에 쓴다.
                while (mIsRecording) {
                    read = mAudioRecord.read(data, 0, BUFFER_SIZE);
                    if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                        mBOStream.write(data);
                    }
                }

                // 녹음이 끝나면 temp파일의 길이를 계산한다.
                mBOStream.flush();
                mAudioLen = (int)tempFile.length();

                // 인풋스트림으로 temp 파일을 설정하고, 아웃풋스트림은 닫는다.
                mBIStream = new BufferedInputStream(new FileInputStream(tempFile));
                mBOStream.close();

                // 아웃풋 스트림을 wave 파일로 새로 설정하고, 헤더를 쓴다.
                mBOStream = new BufferedOutputStream(new FileOutputStream(waveFile));
                mBOStream.write(getFileHeader());
                //len = HEADER_SIZE;

                // 인풋 스트림에 있는 것을 읽어서, 아웃풋 스트림에 쓴다.
                while ((read = mBIStream.read(buffer)) != -1) {
                    mBOStream.write(buffer);
                }

                // 모두 닫는다.
                mBOStream.flush();
                mBIStream.close();
                mBOStream.close();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private byte[] getFileHeader() {
        // temp에 읽어들인 길이 + 헤더길이(40)으로 총 데이터 길이를 잡는다.
        byte[] header = new byte[HEADER_SIZE];
        int totalDataLen = mAudioLen + 40;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * WAVE_CHANNEL_MONO/8;

        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = (byte)1;  // format = 1 (PCM방식)
        header[21] = 0;
        header[22] =  WAVE_CHANNEL_MONO;
        header[23] = 0;
        header[24] = (byte) (RECORDER_SAMPLERATE & 0xff);
        header[25] = (byte) ((RECORDER_SAMPLERATE >> 8) & 0xff);
        header[26] = (byte) ((RECORDER_SAMPLERATE >> 16) & 0xff);
        header[27] = (byte) ((RECORDER_SAMPLERATE >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) RECORDER_BPP * WAVE_CHANNEL_MONO/8;  // block align
        header[33] = 0;
        header[34] = RECORDER_BPP;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte)(mAudioLen & 0xff);
        header[41] = (byte)((mAudioLen >> 8) & 0xff);
        header[42] = (byte)((mAudioLen >> 16) & 0xff);
        header[43] = (byte)((mAudioLen >> 24) & 0xff);
        return header;

    }

    public void stopRecording() {
        if (null != mAudioRecord) {
            mIsRecording = false;
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
        }
    }

    public boolean getIsRecording() {
        return mIsRecording;
    }

    public void setIsRecording( boolean isRecording){
        this.mIsRecording = isRecording;
    }
}
*/


    /* new
    private int mAudioSource = MediaRecorder.AudioSource.MIC;
    private int mSampleRate = 44100;
    private int mChannelCount = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int mBufferSize = AudioTrack.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat);
*/
