package com.yoyo.authentication.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yoyo.authentication.service.TbUserAuthinfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class TbUserAuthinfoServiceImpl implements TbUserAuthinfoService {

    @Value("${rrk.auth.url}")
    private String authurl;

    @Value("${rrk.auth.secret}")
    private String authsecret;
    @Override
    public Object verifyIdentityInfo(String idCard, String realName) {
        String returnStr = addAutoAuthentication(idCard, realName);
        //解析认证结果
        //{"error_code":0,"reason":"认证通过","result":{"realName":"","cardNo":"","details":{"addrCode":"","birth":"","sex":0,"length":,"checkBit":"","addr":"","province":"","city":"","area":""},"isok":1},"ordersign":""}
        //{"error_code":0,"reason":"无此身份证号码","result":{"realName":"测试","cardNo":"422202199511112325","isok":-2},"ordersign":"20190826114737073025748529"}
        JSONObject jsonObject = JSONObject.parseObject(returnStr);
        Object object = jsonObject.get("result");
        Object reason = jsonObject.get("reason");
        if (((int) ((JSONObject) object).get("isok") == -1 ) || (int) ((JSONObject) object).get("isok") == -2) {
            return "姓名和身份证号不匹配";
        } else if ((int) ((JSONObject) object).get("isok") == 1) {
            return reason;
        } else {
            return "姓名和身份证号不匹配";
        }
    }

    //添加自动实名认证
    private String addAutoAuthentication(String idCard, String realName) {
        //构造需要请求的内容
        StringBuffer sb = new StringBuffer();
        sb.append("cardNo=" + idCard);
        sb.append("&realName=" + realName);
        //执行认证请求
        return requestGet(authurl + "/idcard", sb.toString(), authsecret);
    }



    /**
     * 执行认证请求
     *
     * @param strUrl
     * @param param
     * @param appcode
     * @return
     */
    public static String requestGet(String strUrl, String param, String appcode) {
        String returnStr = null; // 返回结果定义
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(strUrl + "?" + param);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET"); // get
            httpURLConnection.setUseCaches(false); // 不用缓存
            httpURLConnection.connect();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            returnStr = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnStr;
    }
}
