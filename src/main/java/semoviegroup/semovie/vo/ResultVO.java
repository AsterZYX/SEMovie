package semoviegroup.semovie.vo;

import lombok.Data;

@Data
public class ResultVO<T> {

    //返回信息编码，成功为0，失败为1
    Integer code;

    //返回信息，备注
    String message;

    //返回数据
    T data;

    public ResultVO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
    
    
    
}
