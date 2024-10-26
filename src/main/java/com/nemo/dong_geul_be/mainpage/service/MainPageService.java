package com.nemo.dong_geul_be.mainpage.service;

import com.nemo.dong_geul_be.mainpage.dto.MainPageBoardResponse;

public interface MainPageService {
    MainPageBoardResponse getDongGeulBoard();
    MainPageBoardResponse getJaeJalBoard();
}
