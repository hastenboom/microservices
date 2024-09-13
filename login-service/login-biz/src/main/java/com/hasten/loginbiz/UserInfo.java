package com.hasten.loginbiz;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

/**
 * @author Hasten
 */
@Data
public class UserInfo {
    @NotEmpty
    private String username;
    @NotEmpty(groups = {ValidationLoginGroup.WithPwd.class})
    private String password;
    @NotEmpty(groups = {ValidationLoginGroup.WithCode.class})
    private String code;
}
