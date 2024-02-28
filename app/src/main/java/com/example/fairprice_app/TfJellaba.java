package com.example.fairprice_app;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TfJellaba {
    private Interpreter interpreter;

    // Input image size expected by the model
    private static final int IMAGE_SIZE_X = 300;
    private static final int IMAGE_SIZE_Y = 300;
    private static final int NUM_CHANNELS = 3;
    private static final int BATCH_SIZE = 1;
    private static final int NUM_CLASSES = 6;

    // Pre-allocated buffers for storing input and output data
    private ByteBuffer inputBuffer = null;
    private ByteBuffer outputBuffer = null;
    private float[][] outputValues = null;

    public TfJellaba(Context context, String modelFileName) throws IOException {
        // Load the TensorFlow Lite model

        interpreter = new Interpreter(loadModelFile(context, modelFileName));
        // Allocate input and output buffers
        inputBuffer = ByteBuffer.allocateDirect(BATCH_SIZE * IMAGE_SIZE_X * IMAGE_SIZE_Y * NUM_CHANNELS * 4);
        inputBuffer.order(ByteOrder.nativeOrder());
        outputBuffer = ByteBuffer.allocateDirect(BATCH_SIZE * NUM_CLASSES * 7);
        outputBuffer.order(ByteOrder.nativeOrder());
        outputValues = new float[BATCH_SIZE][NUM_CLASSES];
    }

    private MappedByteBuffer loadModelFile(Context context, String modelFileName) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelFileName);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public String classifyImage(Bitmap bitmap) {
        Log.d("TfJellaba", "Classifying image...");
        // Resize and preprocess the input image
        Bitmap resizedImage = Bitmap.createScaledBitmap(bitmap, IMAGE_SIZE_X, IMAGE_SIZE_Y, true);
        convertBitmapToByteBuffer(resizedImage);

        // Run inference
        interpreter.run(inputBuffer, outputBuffer);

        // Post-processing: get the predicted class
        outputBuffer.rewind();
        outputBuffer.asFloatBuffer().get(outputValues[0]);
        int predictedClassIndex = argmax(outputValues[0]);

        // Return the class label
        switch (predictedClassIndex) {
            case 1:
                return "140.0-190.0";
            case 2:
                return "200.0-270.0";
            case 3:
                return "280.0-360.0";
            case 4:
                return "379.0-460.0";
            case 5:
                return "470.0-540.0";
            case 6:
                return "560.0-690.0";
            default:
                return "Unknown";
        }
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (inputBuffer == null) {
            return;
        }
        inputBuffer.rewind();
        int[] intValues = new int[IMAGE_SIZE_X * IMAGE_SIZE_Y];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < IMAGE_SIZE_X; ++i) {
            for (int j = 0; j < IMAGE_SIZE_Y; ++j) {
                final int val = intValues[pixel++];
                inputBuffer.putFloat(((val >> 16) & 0xFF) / 255.0f);
                inputBuffer.putFloat(((val >> 8) & 0xFF) / 255.0f);
                inputBuffer.putFloat((val & 0xFF) / 255.0f);
            }
        }
    }

    private int argmax(float[] array) {
        int best = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[best]) {
                best = i;
            }
        }
        return best;
    }
}
