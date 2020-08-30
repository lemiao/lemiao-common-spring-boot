package com.lemiao.common.rpc;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.Logger;

/**
 * @author catface
 * @date 2019-01-23
 */
@Data
@ApiModel(description = "通用返回结果")
public class RpcResult<T> implements Serializable {

    public static final String SUCCESS_CODE = "200";
    public static final String ERROR_CODE = "500";
    public static final String SUCCESS_MESSAGE = "操作成功!";
    public static final String RPC_ERROR = "系统繁忙,请稍后重试!";
    private static final long serialVersionUID = 1305578315208489061L;

    @ApiModelProperty(value = "状态码", example = SUCCESS_CODE, required = true)
    private String code;

    @ApiModelProperty(value = "请求成功与否,true:成功,false:失败", required = true)
    private Boolean success;

    @ApiModelProperty(value = "提示信息", example = SUCCESS_MESSAGE, required = true)
    private String message;

    @ApiModelProperty(value = "返回业务数据")
    private T result;

    private RpcResult() {

    }

    public RpcResult(boolean isSuccess, String code, String message) {
        this.success = isSuccess;
        this.code = code;
        this.message = message;
    }

    public static <T> RpcResult<T> success() {
        return new RpcResult<T>(true, SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public static <T> RpcResult<T> success(T data) {
        RpcResult<T> result = new RpcResult<T>(true, SUCCESS_CODE, SUCCESS_MESSAGE);
        result.setResult(data);
        return result;
    }

    public static <T> RpcResult<T> error(String message) {
        return new RpcResult<T>(false, ERROR_CODE, message);
    }

    public static <T> RpcResult<T> error(T data, String message) {
        RpcResult<T> result = new RpcResult<T>(false, ERROR_CODE, message);
        result.setResult(data);
        return result;
    }

    public static <T> RpcResult<T> rpcError() {
        return new RpcResult<T>(false, ERROR_CODE, RPC_ERROR);
    }

    public static <T> RpcResult<T> rpcError(String message) {
        return new RpcResult<T>(false, ERROR_CODE, message);
    }

    public static <T> RpcResult<T> error(String message, String code) {
        RpcResult<T> result = new RpcResult<T>(false, code, message);
        result.setCode(code);
        return result;
    }

    public static <T> RpcResult<T> rpcError(Logger logger, String rpcErrorMessage, Object request,
                                            Throwable cause) {
        logger.error("rpcError," + rpcErrorMessage + ",request:{}", request, cause);
        return RpcResult.rpcError();
    }

}
