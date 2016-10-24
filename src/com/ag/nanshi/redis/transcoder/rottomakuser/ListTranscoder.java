package com.ag.nanshi.redis.transcoder.rottomakuser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ag.nanshi.redis.transcoder.closeable.CloseableCommon;

public class ListTranscoder {
	
	public static byte[] serialize(Object value) {  
        if (value == null) {  
            throw new NullPointerException("Can't serialize null");  
        }  
        byte[] rv=null;  
        ByteArrayOutputStream bos = null;  
        ObjectOutputStream os = null;  
        try {  
            bos = new ByteArrayOutputStream();  
            os = new ObjectOutputStream(bos);  
            os.writeObject(value);  
            os.close();  
            bos.close();  
            rv = bos.toByteArray();  
        } catch (Exception e) {  
        	e.printStackTrace();
        } finally {  
        	CloseableCommon.close(os);  
        	CloseableCommon.close(bos);  
        }  
        return rv;  
    }  

    public static Object deserialize(byte[] in) {  
        Object rv=null;  
        ByteArrayInputStream bis = null;  
        ObjectInputStream is = null;  
        try {  
            if(in != null) {  
                bis=new ByteArrayInputStream(in);  
                is=new ObjectInputStream(bis);  
                rv=is.readObject();  
                is.close();  
                bis.close();  
            }  
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {  
        	CloseableCommon.close(is);  
        	CloseableCommon.close(bis);  
        }  
        return rv;  
    } 

}
