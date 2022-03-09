package com.example.audiotranscode;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/speak")
public class TaskController {

    public TaskController() {
    }

    @PostMapping("/transcode")
    @ResponseBody
    public ResponseEntity<Object> transcode(@RequestBody TranscodeTask transcodeTask) throws Exception {
        byte[] result = transcodeTask.transcode();
        MediaType mediaType = getMediaType(transcodeTask.getMediaType());
        ByteArrayResource resource = new ByteArrayResource(result);
        return ResponseEntity.ok()
            .contentType(mediaType)
            .contentLength(resource.contentLength())
            .body(resource);
    }

    private MediaType getMediaType(String mediaType) {
        if (mediaType.toLowerCase().equals("mp3")) {
            return new MediaType("audio", "mpeg");
        }
        if (mediaType.toLowerCase().equals("wav")) {
            return new MediaType("audio", "wav");
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

}
