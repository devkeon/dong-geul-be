package com.nemo.dong_geul_be.authentication.util;

import com.nemo.dong_geul_be.authentication.domain.JwtMemberDetail;

public interface SecurityContextUtil {

	JwtMemberDetail getContextMemberInfo();

}
