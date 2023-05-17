package com.trantien.huetutor.payloads;

public class PagingResponse {
    private String status;
    private String message;
    private Long totalPages;
    private Long curPage;
    private Object data;

    public PagingResponse(String status, String message, Long totalPages, Long curPage, Object data) {
        this.status = status;
        this.message = message;
        this.totalPages = totalPages;
        this.curPage = curPage;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getCurPage() {
        return curPage;
    }

    public void setCurPage(Long curPage) {
        this.curPage = curPage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
