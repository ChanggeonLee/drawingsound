package com.a7f.drawingsound.lib;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import ca.uol.aig.fftpack.RealDoubleFFT;

public class RecordAudio extends AsyncTask<Void, double[], Void> {

    private int frequency = 11025;
    private int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private RealDoubleFFT transformer;

    private int blockSize = 1200;
    private boolean started = false;

    private int noteIndex = 0;
    private String[] note;
    private TextView TextViewFFT;

    public RecordAudio( TextView TextViewFFT){
        transformer = new RealDoubleFFT(blockSize);
        this.TextViewFFT = TextViewFFT;
        note = new String[10000];
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
            AudioRecord audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, blockSize);
            short[] buffer = new short[blockSize];
            double[] toTransform = new double[blockSize];

            audioRecord.startRecording();
            while (started) {
                int bufferReadResult = audioRecord.read(buffer, 0, blockSize);
                for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
                    toTransform[i] = (double) buffer[i] / Short.MAX_VALUE; // 부호 있는 16비트
                }
                transformer.ft(toTransform);
                publishProgress(toTransform);
            }
            audioRecord.stop();
        } catch (Throwable t) {
            Log.e("AudioRecord", "Recording Failed");
        }
        return null;
    }

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
        String Note ;
        // 3.3 곱한거에서 +3 -3 범위
        if(250 <= fre && fre <= 269){
            // 261 C4
            Note = "C4";
        }else if(269 < fre && fre <= 285){
            // 277 C#
            Note = "C#";
        }else if(285 < fre && fre <= 301){
            // 293 D
            Note = "D";
        }else if(301 < fre && fre <= 319){
            // 311 D#
            Note = "D#";
        }else if(319 < fre && fre <= 337){
            // 329 E
            Note = "E";
        }else if(337 < fre && fre <= 357){
            // 349 F
            Note = "F";
        }else if(357 < fre && fre <= 377){
            // 369 F#
            Note = "F#";
        }else if(377 < fre && fre <= 399){
            // 391 G
            Note = "G";
        }else if(399 < fre && fre <= 423){
            // 415 G#
            Note = "G#";
        }else if(423 < fre && fre <= 448){
            // 440 A
            Note = "A";
        }else if(448 < fre && fre <= 474){
            // 466 A#
            Note = "A#";
        }else if(474 < fre && fre <= 501){
            // 493 B
            Note = "B";
        }else{
            Note = "X";
        }
        note[noteIndex] = Note;
        noteIndex++;
        Log.d("index",Integer.toString(noteIndex));
        TextViewFFT.setText(Note);
    }
}
