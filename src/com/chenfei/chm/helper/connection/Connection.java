package com.chenfei.chm.helper.connection;

public interface Connection<T> {

    /**
     * 获取T
     * 
     * @return 返回一个T
     */
    T get();

    /**
     * 判断是否结束
     * 
     * @return true：结束；false：没有结束；
     */
    boolean isEnded();
}
