// -*- Mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
//
// Copyright (C) 2012 Opera Software ASA.  All rights reserved.
//
// This file is an original work developed by Opera Software ASA

package com.example.edaixi;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.Handler;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * DirectByteArrayOutputStream, to get buffer of the output stream, without coping data.
 */
class DirectByteArrayOutputStream extends ByteArrayOutputStream {
    DirectByteArrayOutputStream() {
    }

    DirectByteArrayOutputStream(int size) {
        super(size);
    }

    public byte[] getBuf() {
        return buf;
    }
}

public class AndroidHttpRequest implements Runnable {
    public enum Method {
        GET,
        POST;
    }

    public static interface Listener {
        // Following methods will be called in a independent thread.
        void connected(int statusCode, String statusString, Header[] headers);

        void payload(byte[] data, int offset, int length);

        void error(int code, String reason);
    }

    public static class DefaultListener implements Listener {
        @Override
        public void connected(int statusCode, String statusString, Header[] headers) {
        }

        @Override
        public void payload(byte[] data, int offset, int length) {
        }

        @Override
        public void error(int code, String reason) {
        }
    }

    private static final String TAG = "AndroidHttpRequest";

    private final String mUri;
    private Method mMethod = Method.GET;
    private String mBody;
    private List<Header> mHeaders;
    private Listener mListener;

    private AndroidHttpClient mHttpClient;
    private final Context mContext; // context, useful for ssl session cache.

    private Handler mListenerHandler;
    private Thread mThread;
    private volatile boolean mIsCanceled = false;

    public AndroidHttpRequest(Context context, String uri) {
        mContext = context;
        mUri = uri;
    }

    public AndroidHttpRequest setMethod(Method method) {
        mMethod = method;
        return this;
    }

    public AndroidHttpRequest addHeader(String name, String value) {
        if (mHeaders == null) {
            mHeaders = new ArrayList<Header>();
        }
        mHeaders.add(new BasicHeader(name, value));
        return this;
    }

    public AndroidHttpRequest setBody(String body) {
        mBody = body;
        return this;
    }

    /**
     * @param listenerHandler handler to post a Runnable when the listener's method should be called.
     * It can be null, then the listener's method will be called directly in another thread.
     */
    public AndroidHttpRequest setListener(Listener listener, Handler listenerHandler) {
        mListener = listener;
        mListenerHandler = listenerHandler;
        return this;
    }

    public void start() {
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void run() {
        try {
            boolean isPost = mMethod == Method.POST;
            HttpUriRequest httpRequest = isPost ? new HttpPost(mUri) : new HttpGet(mUri);
            httpRequest.addHeader("Accept-Charset", "utf-8");
            httpRequest.addHeader("Content-Type", "application/xml; charset=utf-8");
            if (mHeaders != null) {
                for (Header header : mHeaders) {
                    httpRequest.setHeader(header.getName(), header.getValue());
                }
            }
            if (isPost && mBody != null) {
                ((HttpPost) httpRequest).setEntity(AndroidHttpClient.getCompressedEntity(
                    mBody.getBytes("UTF-8"), mContext.getContentResolver()));
            }
            mHttpClient = AndroidHttpClient.newInstance("Oupeng Android", mContext);
            // ProxyUtils.configHttpClientProxy(mHttpClient, mContext, httpRequest.getURI());
            AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpRequest);
            HttpClientParams.setRedirecting(mHttpClient.getParams(), true);
            HttpResponse response = mHttpClient.execute(httpRequest);
            int responseCode = response.getStatusLine().getStatusCode();
            if (mListener != null) { // if no listener, no need to read headers and body.
                String responseString = response.getStatusLine().getReasonPhrase();
                Header[] headers = response.getAllHeaders();
                onConnected(responseCode, responseString, headers);

                InputStream instream = AndroidHttpClient.getUngzippedContent(response.getEntity());
                DirectByteArrayOutputStream outstream = new DirectByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int read = instream.read(buffer);
                while (read > 0 && !isCanceled()) {
                    outstream.write(buffer, 0, read);
                    read = instream.read(buffer);
                }
                if (!isCanceled()) {
                    onPayload(outstream.getBuf(), 0, outstream.size());
                }
                instream.close();
                outstream.close();
            }
        } catch (UnsupportedEncodingException e) {
            onError(e);
        } catch (IOException e) {
            onError(e);
        } finally {
            closeHttpClient();
            setCanceled(false);
        }
    }

    private void onConnected(final int responseCode, final String responseString,
            final Header[] headers) {
        if (mListener != null && !isCanceled()) {
            handleRunnable(new Runnable() {
                @Override
                public void run() {
                    mListener.connected(responseCode, responseString, headers);
                }
            });
        }
    }

    private void onPayload(final byte[] data, final int offset, final int length) {
        if (mListener != null && !isCanceled()) {
            handleRunnable(new Runnable() {
                @Override
                public void run() {
                    mListener.payload(data, offset, length);
                }
            });
        }
    }

    private void onError(Exception e) {
        Log.e(TAG, "onError", e);
        if (mListener != null && !isCanceled()) {
            final String errorString = "Exception occur";
            handleRunnable(new Runnable() {
                @Override
                public void run() {
                    mListener.error(0, errorString);
                }
            });
        }
    }

    private void handleRunnable(Runnable r) {
        if (mListenerHandler != null) {
            mListenerHandler.post(r);
        } else {
            r.run();
        }
    }

    private synchronized void closeHttpClient() {
        if (mHttpClient != null) {
            mHttpClient.close();
            mHttpClient = null;
        }
    }

    private synchronized boolean isCanceled() {
        return mIsCanceled;
    }

    private synchronized void setCanceled(boolean canceled) {
        mIsCanceled = canceled;
    }

    public void cancel() {
        setCanceled(true);
    }
}
