package com.example.backend.utils.external;

import com.aliyun.tea.*;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyRequest;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponse;
import com.aliyun.teautil.models.RuntimeOptions;

import java.util.Arrays;
import java.util.List;

/**
 * 用于校验用户身份证和名字的一个工具类，但是需要阿里云企业账户才可以使用，故废弃
 * 文档 https://help.aliyun.com/zh/id-verification/?spm=a2c4g.11186623.0.0.7fe8b5bfet1ngP
 */
@Deprecated
public class IdentifyUtils {

    public static void main(String[] args_) throws Exception {

        try {
            // 构建request。
            Id2MetaVerifyRequest request = new Id2MetaVerifyRequest();
            request.paramType = "normal"; // 加密手段，不加密，若要加密请指定并用指定加密手段加密下面俩参数
            request.userName = "身份证号码";
            request.identifyNum = "你的名字";
            // 自动路由服务。
            Id2MetaVerifyResponse response = IdentifyUtils.id2MetaVerifyAutoRoute(request);
            String ret = com.aliyun.teautil.Common.toJSONString(com.aliyun.teautil.Common.toMap(response));
            com.aliyun.teaconsole.Client.log("最终结果（若此处为空，则所有服务点均异常，请逐步调试）：" + ret + "");
        } catch (TeaException error) {
            com.aliyun.teaconsole.Client.error(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            com.aliyun.teaconsole.Client.error(error.message);
        }
    }

    /**
     * <b>description</b> :
     * <p>主备服务点循环调用，获取到成功结果返回。</p>
     */
    public static Id2MetaVerifyResponse id2MetaVerifyAutoRoute(Id2MetaVerifyRequest request) throws Exception {
        List<String> endpoints = Arrays.asList(
            "cloudauth.cn-shanghai.aliyuncs.com",
            "cloudauth.cn-beijing.aliyuncs.com"
        );
        Id2MetaVerifyResponse lastResponse = null;
        for (String endpoint : endpoints) {
            try {
                // 调用服务。
                Id2MetaVerifyResponse response = IdentifyUtils.id2MetaVerify(endpoint, request);
                // 节点调用结果
                String ret = com.aliyun.teautil.Common.toJSONString(com.aliyun.teautil.Common.toMap(response));
                com.aliyun.teaconsole.Client.log("节点 " + endpoint + " 结果：" + ret + " ");
                // 有一个服务调用成功即返回。
                if (!com.aliyun.teautil.Common.isUnset(response) && com.aliyun.teautil.Common.equalNumber(response.statusCode, 200)) {
                    if (!com.aliyun.teautil.Common.isUnset(response.body) && com.aliyun.teautil.Common.equalString(response.body.code, "200")) {
                        lastResponse = response;
                        break;
                    }

                }

            } catch (TeaException error) {
                com.aliyun.teaconsole.Client.error("节点 " + endpoint + " 调用异常：" + error.message + "");
            } catch (Exception _error) {
                TeaException error = new TeaException(_error.getMessage(), _error);
                com.aliyun.teaconsole.Client.error("节点 " + endpoint + " 调用异常：" + error.message + "");
            }
        }
        return lastResponse;
    }

    /**
     * <b>description</b> :
     * <p>获取服务Client实例，调用验证方法。</p>
     */
    public static Id2MetaVerifyResponse id2MetaVerify(String endpoint, Id2MetaVerifyRequest request) throws Exception {
        // 获取SDK Client实例。
        com.aliyun.cloudauth20190307.Client client = IdentifyUtils.createClient(endpoint);
        // 构建RuntimeObject
        RuntimeOptions runtime = new RuntimeOptions();
        runtime.readTimeout = 5000;
        runtime.connectTimeout = 5000;
        // 连接
        return client.id2MetaVerifyWithOptions(request, runtime);
    }

    /**
     * <b>description</b> :
     * <p>安全创建服务Client实例。</p>
     */
    public static com.aliyun.cloudauth20190307.Client createClient(String endpoint) throws Exception {
        // 获取Credential工具，此工具会从环境变量中读取AccessKey。
        com.aliyun.credentials.models.Config credentialConfig = new com.aliyun.credentials.models.Config();
        com.aliyun.credentials.Client credential = new com.aliyun.credentials.Client(credentialConfig);
        // 创建SDK Client实例。
        com.aliyun.teaopenapi.models.Config apiConfig = new com.aliyun.teaopenapi.models.Config();
        apiConfig.credential = credential;
        apiConfig.endpoint = endpoint;
        return new com.aliyun.cloudauth20190307.Client(apiConfig);
    }

}
