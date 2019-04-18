package com.mobvista.okr.vm;

/**
 * @author 顾炜(GUWEI)
 * @date 2018/5/22 17:59
 */
public class CommonRequestVM {

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CommonRequestVM{" +
                "id=" + id +
                '}';
    }
}
