package com.ecnu.compiler.component.CacheManager;

import java.util.HashMap;
import java.util.Map;

public class LanguageCache {
    private static LanguageCache sLanguageCache;

    public static LanguageCache getInstance(){
        if (sLanguageCache == null){
            synchronized (LanguageCache.class){
                if (sLanguageCache == null)
                    sLanguageCache = new LanguageCache();
            }
        }
        return sLanguageCache;
    }

    //缓存映射
    private Map<Integer, Language> mCache;
    //缓存数量上限
    private static final int CACHE_LIMIT = 200;

    public LanguageCache() {
        mCache = new HashMap<>();
    }

    /**
     * 保存至缓存，语言ID为key
     * @param target 缓存语言
     */
    public void saveToCache(Language target){
        mCache.put(target.getId(), target);
    }

    /**
     * 检查对应对象是否被缓存
     * @param languageId 缓存语言ID
     * @return true:语言已经被缓存
     */
    public boolean check(Integer languageId){
        return mCache.containsKey(languageId);
    }

    /**
     * 从缓存中获取对象
     * @param languageId 缓存语言ID
     * @return 如果语言未被缓存则返回null，否则返回对应语言
     */
    public Language getFromCache(Integer languageId){
        return mCache.get(languageId);
    }

    private void adjust(){
        //todo 实现真正的缓存控制，现在直接用清空替代一下，之后可以考虑使用linkedHashMap。
        if (mCache.size() > CACHE_LIMIT)
            mCache.clear();
    }
}
