package com.example.audiotranscode;

import java.io.File;
import java.nio.file.Files;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

public class TranscodeTask {

	private final String AudioUrl;
	private final String MediaType;
    private final int Channels = 2;
    private final int SampleRate = 44100;

	public TranscodeTask(String AudioUrl, String MediaType) {
		this.AudioUrl = AudioUrl;
		this.MediaType = MediaType;
	}

    public String getAudioUrl() {
        return this.AudioUrl;
    }

    public String getMediaType() {
        return this.MediaType;
    }

    public byte[] transcode() throws Exception {
        byte[] result = null;
        FFmpegLogCallback.set();
        File tempFile = File.createTempFile("audio", getSuffix(this.MediaType), null);
        int audioChannels = this.Channels;
        int sampleRate = this.SampleRate;
        int audioCodec = getAudioCodec(this.MediaType);
        FFmpegFrameGrabber grabber =null;
        FFmpegFrameRecorder recorder = null;

        try {
            grabber = new FFmpegFrameGrabber(this.AudioUrl);

            grabber.start();

            recorder = new FFmpegFrameRecorder(tempFile, audioChannels);
            recorder.setAudioCodec(audioCodec);
            recorder.setSampleRate(sampleRate);
            if (this.MediaType.toLowerCase().equals("raw")) {
                recorder.setFormat("data");
            }
            recorder.start();
            Frame audio = null;
            for (; ; ) {
                if ((audio = grabber.grabSamples()) != null) {
                    recorder.recordSamples(audio.sampleRate, audio.audioChannels, audio.samples);
                } else {
                    break;
                }
            }
        } catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
            System.err.println("fail");
        } catch (Exception e) {
            System.err.println("fail");
        } finally {
            if (grabber != null) {
                grabber.close();
            }
            if (recorder != null) {
                recorder.close();
            }
            result = Files.readAllBytes(tempFile.toPath());
            Files.deleteIfExists(tempFile.toPath());
        }
        return result;
    }

    private int getAudioCodec(String mediaType) {
        if (mediaType.toLowerCase().equals("mp3")) {
            return avcodec.AV_CODEC_ID_MP3;
        }
        return avcodec.AV_CODEC_ID_PCM_S16LE;
    }

    private String getSuffix(String mediaType) {
        if (mediaType.toLowerCase().equals("mp3")) {
            return ".mp3";
        }
        return ".wav";
    }
}
