package com.nemo.dong_geul_be.mypage.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class MyPageRequest {

    @Getter
    public static class  MyClubRequest {
        @NotNull(message = "동아리 이름")
        private String clubName;

        @NotNull(message = "학번")
        private String studentId;

        @NotNull(message = "이름")
        private String name;
    }

    @Getter
    public static class ConfirmOrRejectRequest{
        @NotNull
        private String email;
        @NotNull
        private String clubName;
    }


}
