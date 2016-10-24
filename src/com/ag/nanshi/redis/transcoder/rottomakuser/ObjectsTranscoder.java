package com.ag.nanshi.redis.transcoder.rottomakuser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.ag.nanshi.other.rottomak.RotToMakUser;
import com.ag.nanshi.redis.transcoder.closeable.CloseableCommon;

public class ObjectsTranscoder {

	public static byte[] serialize(List<RotToMakUser> value) {  
        if (value == null) {  
            throw new NullPointerException("Can't serialize null");  
        }  
        byte[] rv=null;  
        ByteArrayOutputStream bos = null;  
        ObjectOutputStream os = null;  
        try {  
            bos = new ByteArrayOutputStream();  
            os = new ObjectOutputStream(bos);  
            for(RotToMakUser user : value){  
                os.writeObject(user);  
            }  
            os.writeObject(null);  
            os.close();  
            bos.close();  
            rv = bos.toByteArray();  
        } catch (IOException e) {  
            throw new IllegalArgumentException("Non-serializable object", e);  
        } finally {  
        	CloseableCommon.close(os);  
        	CloseableCommon.close(bos);  
        }  
        return rv;  
    }  

    public static List<RotToMakUser> deserialize(byte[] in) {  
        List<RotToMakUser> list = new ArrayList<>();  
        ByteArrayInputStream bis = null;  
        ObjectInputStream is = null;  
        try {  
            if(in != null) {  
                bis=new ByteArrayInputStream(in);  
                is=new ObjectInputStream(bis);  
                while (true) {  
                    RotToMakUser user = (RotToMakUser) is.readObject();  
                    if(user == null){  
                        break;  
                    }else{  
                        list.add(user);  
                    }  
                }  
                is.close();  
                bis.close();  
            }
        } catch (Exception e) {  
        	e.printStackTrace();
        } finally {  
            CloseableCommon.close(is);  
            CloseableCommon.close(bis);  
        }  
        return list;  
    }  
}
