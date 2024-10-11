//package com.yb.yff.sb.jvm.heap;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Copyright (c) 2024 to 2045  YangBo.
// * All rights reserved.
// * <p>
// * This software is the confidential and proprietary information of
// * YangBo. You shall not disclose such Confidential Information and
// * shall use it only in accordance with the terms of the license
// * agreement you entered into with YangBo.
// *
// * @author : YangBo
// * @Project: SpringbootDemo
// * @Class: OOMHeap
// * @CreatedOn 2024/9/6.
// * @Email: yangboyff@gmail.com
// * @Description: oom
// */
//public class OOMHeap {
//    public static void main(String[] args) {
//        OOMHeap oomHeap = new OOMHeap();
//        oomHeap.testOom2();
//    }
//    public void testOom1() {
//        List<String> list = new ArrayList<>(10);
//        while (true) {
//            list.add("1");
//        }
//    }
//
//    public void testOom2() {
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        for (int i = 0; i < 1000000; i++) {
//            Map<String, Object> map = new HashMap<>();
//            for (int j = 0; j < i; j++) {
//                map.put(String.valueOf(j), j);
//            }
//            mapList.add(map);
//        }
//    }
//
//    public void MetaSpaceDemo() {
//        while (true){
//            Enhancer  enhancer = new Enhancer() ;
//            enhancer.setSuperclass(HeapOOM.class);
//            enhancer.setUseCache(false) ;
//            enhancer.setCallback(new MethodInterceptor() {
//                @Override
//                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//                    return methodProxy.invoke(o,objects) ;
//                }
//            });
//            enhancer.create() ;
//
//        }
//    }
//}
