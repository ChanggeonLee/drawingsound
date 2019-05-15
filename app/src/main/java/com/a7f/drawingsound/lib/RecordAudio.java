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
    private String note;
    private TextView TextViewFFT;

    public RecordAudio( TextView TextViewFFT){
        transformer = new RealDoubleFFT(blockSize);
        this.TextViewFFT = TextViewFFT;
        note = "";
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
//        return note.toString();
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
        String[] AllNote = {"C3","C#3","D3","D#3","E3","F3","F#3","G3","G#3","A3","A#3","B3",
                            "C4","C#4","D4","D#4","E4","F4","F#4","G4","G#4","A4","A#4","B4",
                            "C5","C#5","D5","D#5","E5","F5","F#5","G5","G#5","A5","A#5","B5"};
        String Note ;

        fre = (int)fre;

        // 3.3 곱한거에서 +3 -3 범위
        if(250 <= fre && fre <= 269){
            // 261 C4
            Note = "C";
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
        } else if(501 < fre && fre <= 538){
            // 523 C
            Note = "c";
        }else if(538 < fre && fre <= 570){
            // 554 C#
            Note = "c#";
        }else if(570 < fre && fre <= 609){
            // 587 D
            Note = "d";
        }else if(609 < fre && fre <= 640){
            // 622 D#
            Note = "d#";
        }else if(640 < fre && fre <= 677){
            // 659 E
            Note = "e";
        }else if(677 < fre && fre <= 720){
            // 698 F
            Note = "f";
        }else if(720 < fre && fre <= 762){
            // 740 F#
            Note = "f#";
        }else if(762 < fre && fre <= 807){
            // 784 G
            Note = "g";
        }else if(807 < fre && fre <= 856){
            // 831 G#
            Note = "g#";
        }else if(856 < fre && fre <= 906){
            // 880 A
            Note = "a";
        }else if(906 < fre && fre <= 960){
            // 932 A#
            Note = "a#";
        }else if(960 < fre && fre <= 1018){
            // 988 B
            Note = "b";
        } else{
            Note = "z";
        }

        note += Note;
        //        noteIndex++;
        //        Log.d("index",Integer.toString(noteIndex));
        TextViewFFT.setText(Note);
    }

    public String getNoteData(){
//        String str = String.join(",", note);
        Log.e("inRecordNote",note);
        return note;

    }
}
