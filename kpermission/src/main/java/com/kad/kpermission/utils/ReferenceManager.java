package com.kad.kpermission.utils;


import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 弱引用管理类
 */
public class ReferenceManager {
    private List<WeakReference<Activity>> weakReferenceList;

    private static ReferenceManager referenceManager;

    private ReferenceManager(){
        weakReferenceList = new ArrayList<>();
    }

    public static ReferenceManager with(){

        if(referenceManager == null){
            synchronized (ReferenceManager.class){
                if(referenceManager == null){
                    referenceManager = new ReferenceManager();
                }
            }
        }
        return referenceManager;
    }


    public void  pop(WeakReference<Activity> weakReference){
        weakReferenceList.add(weakReference);
    }

    public void clear(){
        for(int i=0;i<weakReferenceList.size();i++){
            WeakReference<Activity> weakReference = weakReferenceList.get(i);
            if(isValidate(weakReference)){
                weakReference.clear();
                weakReference = null;
            }
        }
        weakReferenceList.clear();
    }

    private boolean isValidate(WeakReference<Activity> weakReference){
        return weakReference!=null &&weakReference.get()!=null;
    }
}
