package com.resto.msgc.backend.card.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KONATA on 2016/11/20.
 * 支付宝工具类
 */
public class AliPayUtils {


//    private static final String baseUrl = "https://openapi.alipaydev.com/gateway.do"; //沙箱环境
    private static final String baseUrl = "https://openapi.alipay.com/gateway.do"; //正式环境

    //测试账号
//    public static final String TRADE_APP_ID = "2016112403199851";
//
//    public static final String TRADE_PRIVATE_KEY = "MIIEuwIBADANBgkqhkiG9w0BAQEFAASCBKUwggShAgEAAoIBAQCdrRxoiE33DxaIQMDoKI+ml9fLbiR+FkNjxw3QQ2rS3a+6YMbW9jMuWoqgh57Yie/9NLOzLXu/LOVvl3CBwY17gXXPVBbszSa4OPE7FBDDpVTQEnJWxGhg5rrftvz+wEvIgpRxMSDHwLiNUMaonpv8TGNgzhNHOGbmFfQXW9/BmyzQsNGTPMKWQGnVE66v60d0pi6LiteWRTf1STeot5zuPrc1pHf86ZE0nOcx0ec1taDvKe9Zntp5ZxtSF8XH548NxDU3IO93V1XhdOLTAWdWtCCyVw5g0mzBnHdO31xjdYg9vSivDVe1zZbc5NgxHFi8uz6Q+ORt6hJ8ZuEExI1bAgMBAAECggEAMOBHbRPLbykLKANCiPSByvdImjOzNi56h0RdwNAVHfZAEbsMB/hbVyQT0r0XfTIM5WVfysvvjwxAtr2vz1kLHVy9Ax6i2JajSPnFtTV8GNl4F+OP89uAK4F6LfzQIbvEAgyQLSI4chMIYi3G1uALpEKdj5VxnmMaanolptQA4UdUUJaJr3wy8QnJIIW7s5bW/sLhj5+gaGLoRcJ2M90urGwfxT/5imPUbXaQJqrJgEsKMPwu+BqerMFKPw3KfckFwnbtuxLaJGjh71pF3YOR9CfHzrwENop7InJwGs6YOd/M4VNJ6/xS/crTXw8qU7JhzQAJfhWJofbuswqRrpWEWQKBgQD4oEnwpbXhdye6bygRCvok7COtJ/VLtrsmWGELqKKKrXRgUOrqeVtxJKN0bw5d3tYQXZNBAA859E0PGCKPkl1LLUWL+BUMCXPYl5zVYEG2ETor0HtctyHFiu0DAThD/u0iGTWl9nbEH8oSiiUIcVRWIA3Bm7pPltwZZzAe4gu8pwKBgQCiWkdSKjRlotlMIcxcdxQCMK2/8v/wz67Bkn0ijsO4v9qn7d03vklF1oyjk4JFZBKoZdtIymAFcNn+gvh7NTS7Uha/GOgpDKRwi5vTvGY0AMnwXUrXEdutyuJ7yRnem9pprqCY3v6aNoJS8HaURYzW+xCuXfZ3aL8NyZnyzQD8LQKBgQCwJ9F7oc1mPCh33X4D5UUql+0HKIKR7JxZJNFNk2Pm4dLM00g+bVGurojpXC5PsCz3V3WX3lTreP6ILU9/bfKP6zmVXGZ9Ks1brsA+Jn8oYrhWhtaA8HgLIM2hDWIyOxMEz9vFBQVNHEDuNdbbToC0rO1SOeYdsgOsNAJSP1KVjQJ/USzSfEKd8DN8cpRzmcfwOSgdt9vIHJ+6wQ59gsnfEoDa0xwbf8Ok30ZnC2K1v/xJV+mVus91M0M5vAfANdpkg0SIGwRZEC1CCwPC5htM/rNmRMhgoyhKrprCoyHpzWQf0Ld/lO/qXTDj+JRCteAQi2z86jIRCaNV447pan0WAQKBgA9Z+sM97rJSRgAhSSxlmNxcUJQGJp1alCMXrSzhGYPniLR9T43j/HteSBiNrOCZqucO9b2cmHMbbps0l57sfTi3le9KKG5NEahDVz1FtiRxgmhmLmFNTRZ+fYZ6dtXjmJYK5AOtq/NmE3COA4SnjDfxihWUr+m6NaSxt9kl2WwF";
//
//    public static final String TRADE_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnReZpVjdPYaVGaH8kjLGK0yp/pidaFPPSiDOQQJxE6zYiAEW3P5TZohuDlO9mE82RP2vt6mr5IobSAObrmuwyZX6T6kDIqtjy7tr9JwVNaHaCTISjQVAd9UJq0vXy8TuFipfLgBVYeQ+N25j8qxwClPZha7FuX5G7rZOxbOMJEHFlWXNVniFOECfiG1MWGko282CHgxCQFQamMO9Ex1qrSBNstiJ97T+rSIT5aRUrzUTP1XGdyOvm98lW8IdgR/S5RupuP8wxXvWqzu9Oc3RBT+4zvuorJrxXIKjwxt9AuwYbxA48t7ZDxwCg0Wjng3BUBlJZV0kScmZrUTTOq8dUQIDAQAB";

    //美食广场账号
//    public static final String TRADE_APP_ID = "2018031302362527";
//
//    public static final String TRADE_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCaEXypEPEsNAY2aSIrWuCi5d1DvsRSOgefl4uaZGS/kn6bjJCgkStsrnUFeM6PqpqfzevyHm+BZep7OckBevbQCjAoAVigfAwBPMRp2QG5lNJTGI8JSxc9WSvfKUnE2UmWs4YIUlmJRB9Rl82D59E2O5mBScfF7WWtSJOr6f4iJWVPwYic/WpQ5hCd968p+V895thNJltukfilfi45LQk047IgU8Cg48OxRmsy2dmSvPLSyU4swQjaC5LUqrrIAnZ08qfbCeazYVa9Ua1mp8nLWyezX+n3xmnY1jUYCn708/dS3zeCV1qJti94fEVyhEV5W/0+qjp9J8by3Ait6AOlAgMBAAECggEAUGIJSWhGfEqUZzUJF9etVjBZ9iuE4GXYbmKx+pagnfb/yYZYOv4iyuxmr7ktBHX9gJz+mm9YvAmBglNO1U/jj/2QzH6PUAQIe2jKDXOkW8AS0DoC/tVHxcFNi3xOOb8ojtERgCv+/Qgm0vpBxq1fUU1dNMBa06CdqQAx08MczZEjximXy/MmQJ1qNg+Ol1y+WWTexJynN+RLuDY0NUqOlpPeAmgNcNfBefOz4mY57fZ9BjcPYcJ5y+NJOymN0kDHNucyUaMJzM9J2NZ3zsJJHPONW99GMmFH3oqRSkF0jGu8NLB1sHxRJVMrkTl5bpibWG3rJzy6IH3KElZWmOqsYQKBgQDzCWjCPPxWg4BqDyBH5aLpAwrGGMzrLvDt+JLcvJ11tSBkAqKz+u4sPxIJ2cvNG1tCq4IaVmJrGQSgOm+Y9kwxr49rO1gSyc1o0CwTubbM6LgAzTeCA15ANBmLjabijIUUa2p2r9TWld1bUtdNC9zByIOPPyDLDp3Dwi/cCn9sqQKBgQCiST3QmRt3msm8qcvJSy+N9HVk9hHwkfVzul+vKqX2gzIsp6jzIE0vooBX/QwhLHjOysbVvFidc6oMlg/4XZfNBxrMnQTmOBPUtaEKKEAHM1OGvGBJsfjQwLKC4xDlo2uf9eDchE0ucnpvPEjWxO3kTyOldcdP18aZjmus08tgnQKBgB0BzH0SZlO1EkiWUZYDNHR3JnwKTbrsOVlNx8mMwYZg/eKx5dVxZ6fkRzThx89NKpFEgxy9nrWFoOW208b/7ownlgiIZrSnXVrbLceU7Np8FCoUVEGyQTeMgFgFqht0BhYv098ikVajQG477vKFLe37Dqi0MifKMM5CiQ/MOPUxAoGARK8M23FBX8CCKzgNyLbqfID/AmjmtNOBd6ZkgsVhElD6dk3LQoLkSAnVmee+L3gb9SF7P9CEDLd8O/Fy9uHdsmeUXw8BmNIZCoiIjLz2RFXjvv2RHv+U1/0GUED70MT2cu/kaFASPlszGEsCZOtZ56va4VdUsKUZJ7pQNgg5Zj0CgYEAtV3iZcOYz0Ttik+gcpSHyfsSssFKoepha7bYvs10gFk//G+rjV74jmT3QPie2jGRb7Z4z7B12yUJ1V2qxA3v/DwzclFaqFhEyP6kmkSun7YE2M/iQmy6ysULtVcISBEkh34BLbIqP7+5GhGI0Gi+Tz4iE4eHA2nhneVnmoFgJaw=";
//
//    public static final String TRADE_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl0V/wVKMOWOjL935pIGdledZAWlm7nHGmAd6IXk4fRlrpg0mT/nQohrFROyoKkaqs1U8e+iT7treGv0z6W+k5iy4+iWO2+hlIfowVdcdoD7nFmeedoQtcX0WF2B+kpSpcYQahvb0+cMtuZkTz7HMmIjfZKLKbzrQmIB+jWmAEWPjyEdmtm/tueSNhaSJ8fKKRM19A7kIWYJb7ojd122LCKHmtUS9TaoGIgfDke5enI6ehdRAamV+FFljPQ/vfX5tsD1r55FhxcfyzHEwibK9JcdFzC+lq3FOVZBWhsRUtih9BFCDO8qvyCqH+YshCtvHYbAtTMANow0QC62WWq2epwIDAQAB";


    public static final Integer ALI_ENCRYPT = 1;

    private static AlipayClient alipayClient;


    public static void connection(String appId, String privateKey, String publicKey, Integer aliEncrypt) {
        if (aliEncrypt == 0) {
            alipayClient = new DefaultAlipayClient(baseUrl, appId, privateKey, "json", "GBK", publicKey);
        } else if (aliEncrypt == 1) {
            alipayClient = new DefaultAlipayClient(baseUrl, appId, privateKey, "json", "GBK", publicKey, "RSA2");
        }
    }

    public static void phonePay(HttpServletResponse httpResponse, Map map, String returnUrl, String nofityUrl) throws Exception {
        JSONObject jsonObject = new JSONObject(map);
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        if(!StringUtils.isEmpty(returnUrl)){
            alipayRequest.setReturnUrl(returnUrl); //支付完成后回调地址
        }

        alipayRequest.setNotifyUrl(nofityUrl);//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent(jsonObject.toString());
        String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        httpResponse.setContentType("text/html;");
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
    }

    /**
     * 面对面支付->条码支付
     * @param json
     * @return
     * @throws AlipayApiException
     */
    public static Map<String, Object> tradePay(JSONObject json) throws AlipayApiException {
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
        request.setBizModel(model);

        model.setOutTradeNo(json.getString("out_trade_no"));
        model.setSubject(json.get("subject").toString());
        model.setTotalAmount(json.getString("total_amount"));
        model.setAuthCode(json.getString("auth_code"));//沙箱钱包中的付款码
        model.setScene(json.getString("scene"));

        AlipayTradePayResponse response = null;
        Map result = new HashMap();
        try {
            response = alipayClient.execute(request);
            if(response.isSuccess()){
                result.put("success", true);
                result.put("msg", response.getOutTradeNo());
            }else{
                result.put("success", false);
                result.put("msg", response.getSubMsg());
                result.put("sub_code", response.getSubCode());
            }
            return result;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "付款出错，请线下处理");
            return result;
        }
    }


    /**
     * 面对面支付->查询订单状态
     * @param jsonObject
     * @return
     */
    public static Map<String, Object> tradeQuery(JSONObject jsonObject)  {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        Map result = new HashMap();
        request.setBizContent(jsonObject.toString());
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            if(response.isSuccess()){
                result.put("success", true);
                result.put("msg", response.getBody());
                result.put("trade_status", response.getTradeStatus());
                result.put("trade_no", response.getTradeNo());
                result.put("total_amount", response.getTotalAmount());
                return result;
            }else{
                result.put("success", false);
                result.put("msg", response.getSubMsg());
                result.put("sub_code", response.getSubCode());
                return result;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "查询出错，请线下处理");
            return result;
        }

    }

    /**
     * 面对面支付撤销订单接口
     * @param jsonObject
     * @return
     */
    public static Map<String, Object> tradeCancel(JSONObject jsonObject){
        //返回的map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", false);
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        //封装订单信息
        request.setBizContent(jsonObject.toJSONString());
        AlipayTradeCancelResponse response = null;
        try{
            response = alipayClient.execute(request);
            if (response.isSuccess()){
                resultMap.put("success", true);
            }else{
                if (StringUtils.isNotBlank(response.getSubMsg())){
                    resultMap.put("msg", response.getSubMsg());
                } else{
                    resultMap.put("msg", "撤销失败，请线下处理");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("msg", "撤销出错，请线下处理");
        }
        return resultMap;
    }


    public static String refundPay(Map map){
        JSONObject jsonObject = new JSONObject(map);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(jsonObject.toString());
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return e.getMessage();

        }
        return response.getBody();
    }

    public static void main(String[] args) throws AlipayApiException {
        connection("2016102800773386",
                        "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANP6llbFMPEF+Kzn" +
                        "ZbYUF4Tw30zVIR15geFQWCCYMveflqCyCIYzDctEBhoeXj674Nofv3xyIxVZSTPe" +
                        "SpGOhjjZRDke1+M++8AUZlIIdDZfkOuoFSxeGYja5m2hWCvUvVg8YbnspRuwjda+" +
                        "fYeWsk6/aDukpULZqTgkii3f2reXAgMBAAECgYBUXlQfzPQhueKzzpVo1q5Vtxjp" +
                        "F5rKhGXxK20n6+u9KsNkyfcikodW84gKNTQFe/mOVzx7Z2IXSSYdgsfjDvrUQ72G" +
                        "UWBESSd+g91SmP0sLwTBk7kQDTzPyNXTKDWdK9ouC3Oho5i+2FLGrdBLizozQpEC" +
                        "ApNbPeSmiy7dGil+gQJBAPeb3dIH4kSWZWkjdBM5RPBARpZAx1PSyJtHlVR+z2TW" +
                        "1mPmT9lbecZlPt2YHTvxGClWZ9nwpxcYi3QasCVf93ECQQDbKZzA6WMl2z+gbXoQ" +
                        "PepJvHDaIt+KhegeRlujAyg1ugx89KvC3UHNF4ajd8FQsAh9c88fzY79LZVpNUTf" +
                        "g2uHAkEA7Pc+UsM4yGsmonhLnhow37yj0Sgtmwse8XyQbUzvLpJsmy7PPDVPVY+P" +
                        "moL5d2REu0r2GJ03S+Mxkuv3p80wAQJBAI8G/yfeqDgCd+moyKpk3cu1USjq7Vwn" +
                        "u65WWGNwIgO+IXxC6P1JDDJekh2If/66gy/sLlYg/po373QzsXj0+W0CQQCu11Ly" +
                        "56Wz2g/8oHFxHgXy0B1QpUyw8nLq5zoUnPXGx+w1pSA+dtXHo9E+cyFq4l1SWQ6B" +
                        "KsZHaJMNcA9svoJi",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB"
        , 0);
        Map map = new HashMap();
        map.put("out_trade_no","ac10efee57e04a30b3f2cbd0652ee5d3");
        map.put("trade_no","2016112121001004850200065496");
        map.put("refund_amount","0.01");
        refundPay(map);
    }
}
