package com.github.jolinzhang.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Shadow on 11/23/16.
 */

public class Util {

    private static Util instance = new Util();
    private Util() {}

    public Util getInstance() { return instance; }

    public byte[] InputStream2ByteArray(InputStream is) {
        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
