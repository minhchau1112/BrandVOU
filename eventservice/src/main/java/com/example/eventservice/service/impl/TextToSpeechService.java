package com.example.eventservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TextToSpeechService {
    private final Cloudinary cloudinary;

    public String textToSpeech(String text) {
        TextToSpeechClient textToSpeechClient = null;
        try {
            // Create a TextToSpeechClient
            textToSpeechClient = TextToSpeechClient.create();

            // Set input for text-to-speech synthesis
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            // Select the voice parameters
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            // Specify audio format
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            // Perform text-to-speech request
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();

            // Convert the audio content into a byte array
            byte[] audioBytes = audioContents.toByteArray();

            // Ensure the byte array is uploaded properly by converting to InputStream with Apache Commons IO
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioBytes);

            // Convert InputStream to byte[] if needed
            byte[] audioData = IOUtils.toByteArray(byteArrayInputStream);

            // Upload the file to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(audioData, ObjectUtils.asMap(
                    "resource_type", "auto"));

            // Print the upload result for debugging
            System.err.println("uploadResult: " + uploadResult);

            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            // Log any errors during text-to-speech or Cloudinary upload
            System.err.println("Failed to create TextToSpeechClient or upload to Cloudinary: " + e.getMessage());
            return null;
        } finally {
            // Close the TextToSpeechClient if it's initialized
            if (textToSpeechClient != null) {
                textToSpeechClient.close();
            }
        }
    }
}
