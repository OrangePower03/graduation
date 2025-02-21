package com.example.backend.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.constants.HttpConstants;
import com.example.backend.utils.web.AppHttpCode;
import com.example.backend.utils.web.WebUtils;

public class PageUtils {

    public static int getPageNum() {
        String pageNum = WebUtils.getRequest().getHeader(HttpConstants.HEADER_PAGE_NUM);
        AssertUtils.nonNull(pageNum, AppHttpCode.PAGE_PARAM_FOUND_ERROR);
        return Integer.parseInt(pageNum);
    }

    public static int getPageSize() {
        String pageSize = WebUtils.getRequest().getHeader(HttpConstants.HEADER_PAGE_SIZE);
        AssertUtils.nonNull(pageSize, AppHttpCode.PAGE_PARAM_FOUND_ERROR);
        return Integer.parseInt(pageSize);
    }

    public static <E> Page<E> getPage() {
        int pageNum = getPageNum();
        int pageSize = getPageSize();
        AssertUtils.isTrue(pageNum > 0, AppHttpCode.PAGE_PARAM_ERROR);
        AssertUtils.isTrue(pageSize > 0 && pageSize <= 50, AppHttpCode.PAGE_PARAM_ERROR);
        return new Page<>(pageNum, pageSize);
    }
}
